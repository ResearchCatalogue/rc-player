/* AudioRegion.js
 * (rc-player)
 *
 * A class for playing sound files or fragments thereof.
 */

rc.AudioRegion = function AudioRegion(sound) {
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
        var c  = null;
        for (var idx = 0; idx < cs.length; idx++) {
            c = cs[idx];
            if (c.source == source && c.sink == sink) {
                ci = idx;
                break;
            }
        }
        c.source.disconnect(c.sink);
        cs.splice(ci, 1);
    };

    self._disconnectAll = function() {
        var cs = self._connections;
        for (var idx = 0; idx < cs.length; idx++) {
            var c = cs[idx];
            c.source.disconnect(c.sink);
        }
        cs.length = 0;
        self._connected = false;
    };

    self._playTime  = 0.0;
    self._playing   = false;

    self._scheduled = [];

    self._schedule = function(fun, dly) {
        var token = -1;
        var fun1 = function() {
            var s       = self._scheduled;
            var ti = -1;
            for (var idx = 0; idx < s.length; idx++) {
                if (s[idx] == token) {
                    ti = idx;
                    break;
                }
            }
            if (ti >= 0) {
                s.splice(ti, 1);
                fun();
            }
        };
        token = window.setTimeout(fun1, dly * 1000);
        self._scheduled.push(token);
    };

    self._clearSchedule = function() {
        var s = self._scheduled;
        for (var idx = 0; idx < s.length; idx++) {
            window.clearTimeout(s[idx]);
        }
        s.length = 0;
    };

    self._doPlay = function() {
        rc.log("do-play " + sound.src);
        var audio       = self._elem;
        var totalDur    = audio.duration;
        var start       = sound.start ? Math.max(0.0  , Math.min(totalDur, sound.start)) : 0.0;
        var stop        = sound.stop  ? Math.max(start, Math.min(totalDur, sound.stop )) : totalDur;
        var dur         = stop - start;
        if (!self._connected) {
            var context = rc.AudioContext();
            if (self._needsGain) {
                var g       = context.createGain();
                self._outNode = g;
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
                self._outNode = self._mediaNode;
                self._connect(self._mediaNode, context.destination);
            }
            self._connected = true;
        }
        audio.play();
        if (dur < totalDur) {
            // if we use the envelope and no loop, schedule a bit later
            var dly = sound.loop || !self._needsGain ? dur : dur + 0.1;
            self._schedule(self._ended, dly);
        }
    };

    self._ended = function() {
        rc.log("ended " + sound.src);
        var repeat = self._playing && !self._releasing;
        self.stop();
        if (sound.loop && repeat) self.play();
    };

    self._eventFun = function(e) {
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
            self._ended();
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
        rc.log("play " + sound.src);
        if (self._playing) {
            self._stop1();
        }
        self._playing = true;
        var audio = self._elem;
        if (audio.readyState >= 2) self._doPlay();
    };

    self.stop = function() {
        rc.log("stop " + sound.src);
        self._stop1();
    };

    self._stop1 = function() {
        self._playing   = false;
        self._releasing = false;
        self._clearSchedule();
        var audio = self._elem;
        if (!audio.paused) audio.pause();
        // reset position
        if (sound.start && sound.start > 0) {
            audio.currentTime = sound.start;
        }
        self._disconnectAll();
    };

    self.release = function(dur) {
        rc.log("release " + dur + " " + sound.src);
        if (self._playing ) {
            self._release1(dur);
            // self._schedule(self.stop, dur + 0.1);
        }
    };

    self._release1 = function(dur) {
        var outNode     = self._outNode;
        var context     = outNode.context;
        var t0          = context.currentTime;
        var t1          = t0 + dur;
        var f           = context.createGain();
        f.gain.setValueAtTime           (1.0, t0);
        f.gain.linearRampToValueAtTime  (0.0, t1);
        // Ok, here's the deal. Bloody Firefox
        // only behaves correctly if we obey the
        // following order of connections and
        // disconnections. First connect the
        // fading synth to output, then reconnect
        // the original tail synth.
        self._connect   (f      , context.destination);
        self._disconnect(outNode, context.destination);
        self._connect   (outNode, f);
        self._outNode   = f;
        self._releasing = true;
    };

    self.releaseAndDispose = function(dur) {
        if (self._playing /* && self._needsGain */) {
            self._release1(dur);
            window.setTimeout(self.dispose, (dur + 0.1) * 1000);
        } else {
            self.dispose();
        }
    };

    self._disposed = false;

    self.dispose = function() {
        rc.log("dispose " + sound.src);
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
};