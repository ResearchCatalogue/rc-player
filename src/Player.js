var rc = {
    dbamp: function(x) {
        return Math.pow(10, x * 0.05);
    },

    AudioContext: function() {
        if (!rc._context) rc._context = new window.AudioContext;
        return rc._context;
    },

    AudioRegion: function AudioRegion(sound) {
        if (!(this instanceof AudioRegion)) {
            return new AudioRegion(sound);
        }

        var self = this;

        if (!sound.fadein ) sound.fadein  = { duration: 0.0 };
        if (!sound.fadeout) sound.fadeout = { duration: 0.0 };
        if (!sound.gain   ) sound.gain    = 0.0;

        var audio       = document.createElement("AUDIO");
        self._elem      = audio;
        self._events    = ["loadedmetadata", "canplay", "durationchange", "emptied", "ended", "error",
                            "loadstart", "progress", "timeupdate"];
        self._mediaNode = rc.AudioContext().createMediaElementSource(audio);
        self._connected = false;
        self._needsGain =
            (sound.fadein .duration > 0) ||
            (sound.fadeout.duration > 0) ||
            (sound.gain > 0) ||
             sound.stop;

        self._connections = [];

        self.mediaNode = function() { return self._mediaNode };

        self._connect = function(source, sink) {
            source.connect(sink);
            self._connections.push({ source: source, sink: sink });
        };

        self._disconnect = function(source, sink) {
            var cs = self._connections;
            var ci = -1;
            for (var idx = 0; idx < cs.length; idx++) {
                if (cs[idx].source == source && cs[idx].sink == sink) {
                    ci = idx;
                    break;
                }
            }
            cs[ci].source.disconnect(cs[ci].sink);
            cs.splice(ci, 1);
        };

        self._disconnectAll = function() {
            var cs = self._connections;
            for (var idx = 0; idx < cs.length; idx++) {
                cs[idx].source.disconnect(cs[idx].sink);
            }
            self._connections.length = 0;
            self._connected = false;
        };

        self._playTime = 0.0;

        self._doPlay = function() {
            var audio = self._elem;
            if (!self._connected) {
                var context = rc.AudioContext();
                if (self._needsGain) {
                    var g       = context.createGain();
                    self._gainNode = g;
                    var amp = rc.dbamp(Math.max(0, sound.gain ? sound.gain : 0.0));
                    var t0  = context.currentTime;
                    self._playTime = t0;
                    var fi  = sound.fadein;
                    if (fi.duration > 0) {
                        var isExpI  = fi.type == "exponential";
                        var lowI    = isExpI ? rc.dbamp(-60) : 0.0;
                        g.gain.setValueAtTime(lowI, t0);
                        var t1      = t0 + fi.duration;
                        if (isExpI) g.gain.exponentialRampToValueAtTime(amp, t1);
                        else        g.gain.linearRampToValueAtTime     (amp, t1);
                    }
                    var totalDur    = audio.duration;
                    var start       = sound.start ? Math.max(0.0, Math.min(totalDur, sound.start)) : 0.0;
                    var stop        = sound.stop  ? Math.max(start, Math.min(totalDur, sound.stop)) : totalDur;
                    var dur         = stop - start;
                    var fo  = sound.fadein;
                    if (fo.duration > 0 && isFinite(dur)) {
                        var isExpO  = fo.type == "exponential";
                        var lowO    = isExpO ? rc.dbamp(-60) : 0.0;
                        var t3      = t0 + dur;
                        var t2      = t3 - fo.duration;
                        g.gain.setValueAtTime(amp, t2);
                        if (isExpO) g.gain.exponentialRampToValueAtTime(lowO, t3);
                        else        g.gain.linearRampToValueAtTime     (lowO, t3);
                    } else if (stop < totalDur) {
                        var t4      = t0 + dur;
                        g.gain.setValueAtTime(0.0, t4);
                    }
                    self._connect(self._mediaNode, g);
                    self._connect(g, context.destination);
                } else {
                    self._connect(self._mediaNode, context.destination);
                }
                self._connected = true;
            }
            audio.play();
        };

        self._eventFun  = function(e) {
            var audio = self._elem;
            if (e.type == "loadedmetadata" && audio.paused) {
                // console.log("loadedmetadata - readyState is " + audio.readyState);
                if (sound.start && sound.start > 0) {
                    audio.currentTime = sound.start;
                }
            } else if (e.type == "canplay" && audio.paused) {
                // console.log("canplay - readyState is " + audio.readyState);
                if (self._playing) self._doPlay();
            } else if (e.type == "ended") {
                self.stop();
            }
            // console.log(e.type);
        };

        for (var i = 0; i < self._events.length; i++) {
            audio.addEventListener(self._events[i], self._eventFun);
        }

        audio.preload   = "auto";
        audio.src       = sound.src;
        if (sound.gain && sound.gain < 0) { // N.B. only attenuation supported here
            audio.volume = rc.dbamp(sound.gain);
        }

        self.playing = function() { return self._playing };

        self.play = function() {
            self._playing = true;
            var audio = self._elem;
            if (audio.readyState >= 2) self._doPlay();
        };

        self.stop = function() {
            self._playing = false;
            var audio = self._elem;
            if (!audio.paused) audio.pause();
            // reset position
            if (sound.start && sound.start > 0) {
                audio.currentTime = sound.start;
            }
            self._disconnectAll();
        };

        self.release = function(dur) {
            if (self._playing) {
                if (self._needsGain) {
                    var g           = self._gainNode;
                    var context     = g.context;
                    var t0          = g.context.currentTime;
                    var t1          = t0 + dur;
                    var f           = context.createGain();
                    f.gain.setValueAtTime           (1.0, t0);
                    f.gain.linearRampToValueAtTime  (0.0, t1);
                    self._connect   (g, f);
                    self._disconnect(g, context.destination);
                    self._connect   (f, context.destination);
                    self._gainNode  = f;

                } else {
                    self.stop();
                }
            }
        };

        self.releaseAndDispose = function(dur) {
            if (self._playing && self._needsGain) {
                self.release(dur);
                window.setTimeout(self.dispose, (dur + 0.1) * 1000);
            } else {
                self.dispose();
            }
        };

        self._disposed = false;

        self.dispose = function() {
            if (!self._disposed) {
                var audio = self._elem;
                for (var i = 0; i < self._events.length; i++) {
                    audio.removeEventListener(self._events[i], self._eventFun);
                }

                self.stop();
                audio.src = "";
                audio.load();
                self._disposed = true;
            }
        }
    }
};