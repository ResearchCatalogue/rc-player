var rc = {
    dbamp: function(x) {
        return Math.pow(10, x * 0.05);
    },

    AudioContext: function() {
        if (!rc._context) rc._context = new window.AudioContext;
        return rc._context;
    },

    AudioRegion: function AudioRegion(sound) {
        if (!(this instanceof AudioRegion)) { // bloody hell, this language is the worst abomination ever
            return new AudioRegion(sound);
        }

        var self = this;    // bloody hell, this language is the worst abomination ever

        var audio       = document.createElement("AUDIO");
        self._elem      = audio;
        self._events    = ["loadedmetadata", "canplay", "durationchange", "emptied", "ended", "error",
                            "loadstart", "progress", "timeupdate"];
        self._mediaNode = rc.AudioContext().createMediaElementSource(audio);
        self._connected = false;
        self._needsGain =
            (sound.fadein  && sound.fadein .duration > 0) ||
            (sound.fadeout && sound.fadeout.duration > 0) ||
            (sound.gain    && sound.gain > 0) ||
             sound.stop;

        self._connections = [];

        self._connect = function(source, sink) {
            source.connect(sink);
            self._connections.push({ source: source, sink: sink });
        };

        self._disconnectAll = function() {
            var c = self._connections;
            for (var i = 0; i < c.length; i++) {
                c[i].source.disconnect(c[i].sink);
            }
            self._connections.length = 0;
            self._connected = false;
        };

        self._doPlay = function() {
            var audio = self._elem;
            if (!self._connected) {
                var context = rc.AudioContext();
                if (self._needsGain) {
                    var g       = context.createGain();
                    self._gainNode = g;
                    var amp = rc.dbamp(Math.max(0, sound.gain ? sound.gain : 0.0));
                    var t0  = context.currentTime;
                    var fi  = sound.fadein;
                    if (fi && fi.duration > 0) {
                        var isExp   = fi.type == "exponential";
                        var low     = isExp ? rc.dbamp(-60) : 0.0;
                        g.gain.setValueAtTime(low, t0);
                        var t1      = t0 + fi.duration;
                        if (isExp) g.gain.exponentialRampToValueAtTime(amp, t1);
                        else       g.gain.linearRampToValueAtTime     (amp, t1);
                    }
                    var totalDur    = audio.duration;
                    var start       = sound.start ? Math.max(0.0, Math.min(totalDur, sound.start)) : 0.0;
                    var stop        = sound.stop  ? Math.max(start, Math.min(totalDur, sound.stop)) : totalDur;
                    var dur         = stop - start;
                    var fo  = sound.fadein;
                    if (fo && fo.duration > 0 && isFinite(dur)) {
                        var isExp   = fo.type == "exponential";
                        var low     = isExp ? rc.dbamp(-60) : 0.0;
                        var t2      = t0 + dur;
                        var t1      = t2 - fo.duration;
                        g.gain.setValueAtTime(amp, t1);
                        if (isExp) g.gain.exponentialRampToValueAtTime(low, t2);
                        else       g.gain.linearRampToValueAtTime     (low, t2);
                    } else if (stop < totalDur) {
                        var t2      = t0 + dur;
                        g.gain.setValueAtTime(0.0, t2);
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

        self.dispose = function() {
            var audio = self._elem;
            for (var i = 0; i < self._events.length; i++) {
                audio.removeEventListener(self._events[i], self._eventFun);
            }

            self.stop();
            audio.src = "";
            audio.load();
        }
    }
};