/* AudioRegion.js
 * (rc-player)
 */

/**
 *  A class for playing sound files or fragments thereof.
 *
 *  Note that events are currently all dispatched from
 *  `audioElem`, not the region itself.
 *
 * @param sound    an object with the sound parameter configuration.
 * @returns {rc.AudioRegion}
 */
rc.AudioRegion = function AudioRegion(sound) {
    if (!(this instanceof AudioRegion)) {
        return new AudioRegion(sound);
    }

    var self = this;

    // ---------------- public ----------------

    /**
     * Returns the logical status. I.e. after
     * calling `play()` this is `true`, after
     * `stop()` this is `false`.
     */
    self.playing = function() { return self._playing };

    self.model = function() { return self._model };

    /**
     * Gets or sets the current relative time offset
     * in seconds from the region's start.
     * `currentTime()` queries and `currentTime(x)` sets the time.
     */
    self.currentTime = function(x) {
        var m = self.model();
        if (x == undefined) {
            return m.currentTime() - sound.start;
        } else {
            m.currentTime(sound.start + x);
        }
    };

    /**
     * Returns the duration of the region in seconds.
     */
    self.duration = function() {
        var m           = self.model();
        var totalDur    = m.duration();
        var start       = Math.max(0.0, Math.min(totalDur, sound.start));
        var stop        = sound.stop ? Math.max(start, Math.min(totalDur, sound.stop )) : totalDur;
        return stop - start;
    };

    /**
     * Gets or sets the volume.
     * E.g. `mySound.volume()` to query and `mySound.volume(0.5)` to update.
     *
     * @param x if undefined, gets the current volume,
     *          if a defined number, sets the volume.
     */
    self.volume = function(x) {
        if (x == undefined) {
            return self._volume;
        } else if (self._volume != x) {
            self._volume = x;
            if (self._connected) {
                var g   = self._gainNode;
                var y   = rc.dbamp(sound.gain) * x;
                var t0  = g.context.currentTime;
                rc.log("volume " + sound.src + " - " + x);
                g.gain.setValueAtTime(y, t0);
            }
            $(self.audioElem()).trigger("volumechange")
        }
    };

    /**
     * Puts the region into playing mode.
     * If the media element is ready to play,
     * playing commences immediately, otherwise
     * it will begin as soon as enough buffered
     * data is available.
     *
     * If the region is currently playing, calling
     * `play` again will result in an abrupt `stop()`
     * before beginning playback again.
     */
    self.play = function() {
        if (self._disposed) return;

        rc.log("play " + sound.src);
        if (self._playing) {
            self._stop1(true);
        }
        self._playing = true;
        var m = self.model();
        if (m.readyState() >= 2) self._doPlay();
    };

    self.pause = function() {
        if (self._disposed) return;

        rc.log("pause " + sound.src);
        self._stop1(false);
    };

    /** Stops the playback of the region immediately. */
    self.stop = function() {
        if (self._disposed) return;

        rc.log("stop " + sound.src);
        self._stop1(true);  // XXX TODO -- should be false in general, but needed for loops
    };

    /**
     * Issues a stop preceded by a fade out
     *
     * @param dur  the fade-out duration in seconds
     */
    self.release = function(dur) {
        rc.log("release " + dur + " " + sound.src);
        if (self._playing) {
            self._release1(dur);
            self._schedule(self._ended, dur + 0.1);
        }
    };

    /**
     * Destroys this audio region, stopping any
     * currently playing node, clearing the scheduler
     * and releasing resources as far as possible.
     * It is not possible to use this audio region
     * beyond this call.
     */
    self.dispose = function() {
        rc.log("dispose " + sound.src);
        if (!self._disposed) {
            var m = self.model();
            var e = m.elem();
            for (var i = 0; i < self._events.length; i++) {
                e.removeEventListener(self._events[i], self._eventFun);
            }

            self.stop();
            m.src("");
            m.load();
            self._disposed = true;
        }
    };

    /**
     * Like `release()` but with a successive `dispose()`.
     * This can be used to "fade-out and forget" this region.
     *
     * @param dur   the fade-out duration in seconds.
     */
    self.releaseAndDispose = function(dur) {
        if (self._disposed) return;

        if (self._playing /* && self._needsGain */) {
            self._release1(dur);
            window.setTimeout(self.dispose, (dur + 0.1) * 1000);
        } else {
            self.dispose();
        }
    };

    /** Returns the media-element-source `AudioNode` on which this region is based. */
    self.mediaNode = function() { return self.model().mediaNode() };

    /** Returns the <audio> element. */
    self.audioElem = function() { return self.model().elem() };

    // ---------------- private ----------------

    /* Initialization. This must only be called from the
     * very bottom of the constructor after all other
     * functions have been defined!
     */
    self._init = function() {
        // fill in missing sections with defaults
        if (!sound.fadein ) sound.fadein  = { duration: 0.0 };
        if (!sound.fadeout) sound.fadeout = { duration: 0.0 };
        if (!sound.gain   ) sound.gain    = 0.0;
        if (!sound.start  ) sound.start   = 0.0;

        // a dedicated gain node is needed if there
        // are fade-ins or the gain is greater than zero decibels,
        // or the sound stops before the end of the sound file.
        self._needsGain = true;

        // N.B. Mozilla doesn't allow us to change the
        // <audio>'s volume once it's connected to WAA,
        // therefore we must always make a gain node.

        /*
            (sound.fadein .duration > 0) ||
            (sound.fadeout.duration > 0) ||
            (sound.gain > 0) ||
             sound.stop;
        */

        self._needsFade =
            (sound.fadein .duration > 0) ||
            (sound.fadeout.duration > 0) ||
            sound.stop;

        // `true` : old behaviour using createMediaElementSource.
        // `false`: new behaviour using AJAX and completely buffered sound.
        var useStreaming = false;

        if (useStreaming) {
            self._model = rc.AudioStream();
        } else {
            self._model = rc.AudioBuffer();
        }

        var m = self.model();
        var e = m.elem();

        for (var i = 0; i < self._events.length; i++) {
            e.addEventListener(self._events[i], self._eventFun);
        }

        m.preload("auto");
        m.src(sound.src);
    };

    // the events that we wish to observe from the media element.
    //self._events        = ["loadedmetadata", "canplay", "durationchange", "emptied", "ended", "error",
    //    "loadstart", "progress", "timeupdate"];
    self._events        = ["loadedmetadata", "canplay", "ended"];
    self._connected     = false;
    self._connections   = [];

    /* Connects two audio nodes and remembers the connection. */
    self._connect = function(source, sink) {
        source.connect(sink);
        self._connections.push({ source: source, sink: sink });
    };

    /* Disconnects two audio nodes have been connected via `_connect`. */
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

    /* Disconnects all remembered connections.
     * Sets `_connected` to `false`. */
    self._disconnectAll = function() {
        var cs = self._connections;
        for (var idx = 0; idx < cs.length; idx++) {
            var c = cs[idx];
            c.source.disconnect(c.sink);
        }
        cs.length = 0;
        self._connected = false;
        $(self.audioElem()).trigger("disconnected");
    };

    // Audio-clock time when `doPlay` was invoked. This is currently not used. */
    self._playTime  = 0.0;

    // Logical status of playing. The actual audio chain might not be ready
    // yet, this flag merely reflects the intention of the caller.
    self._playing   = false;
    // True between `release()` and `stop()`
    self._releasing = false;
    // True after `dispose()`.
    self._disposed  = false;

    // Keeps track of functions scheduled via `_schedule`, so they may be
    // disposed before time.
    self._scheduled = [];

    self._volume = 1.0;

    /* Schedules a function to be executed after a time `dly` given in seconds.
     *
     * If we discover that drift against audio-clock is not negligible for
     * common scenarios, we have to change the implementation to use a audio-node
     * timer with a final short `setTimeout` call.
     */
    self._schedule = function(fun, dly) {
        var token = -1;
        var fun1 = function() {
            var s  = self._scheduled;
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

    /* Cancels all currently scheduled functions. */
    self._clearSchedule = function() {
        var s = self._scheduled;
        for (var idx = 0; idx < s.length; idx++) {
            window.clearTimeout(s[idx]);
        }
        s.length = 0;
    };

    // this stores the 'head' node of the audio graph,
    // i.e. the one that goes to `destination`. This
    // is needed for `release` which inserts a fade
    // node between the `_outNode` and the `destination`.
    self._outNode   = undefined;

    self._gainNode  = undefined;

    self._addTail = function(node) {
        self._connect(self._outNode, node);
        self._outNode = node;
    };

    /* Establishes the audio connections (if necessary)
     * and calls `play` on the media node.
     */
    self._doPlay = function() {
        rc.log("do-play " + sound.src);
        var m           = self.model();
        var totalDur    = m.duration();
        var start       = Math.max(0.0, Math.min(totalDur, sound.start));
        var stop        = sound.stop ? Math.max(start, Math.min(totalDur, sound.stop )) : totalDur;
        var dur         = stop - start;
        if (!self._connected) {
            var context     = rc.AudioContext();
            var t0          = context.currentTime;
            self._playTime  = t0;
            self._outNode   = m.mediaNode();
            if (self._needsGain) {
                console.log("needsGain");
                var g = context.createGain();
                // <audio>.volume broken in Mozilla
                // var amp = rc.dbamp(Math.max(0, sound.gain ? sound.gain : 0.0));
                var amp = rc.dbamp(sound.gain) * self._volume;
                g.gain.setValueAtTime(amp, t0);
                self._gainNode  = g;
                self._addTail(g);
            }
            if (self._needsFade) {
                var f   = context.createGain();
                var fi  = sound.fadein;
                if (fi.duration > 0) {
                    var isExpI  = fi.type == "exponential";
                    var lowI    = isExpI ? rc.dbamp(-60) : 0.0;
                    f.gain.setValueAtTime(lowI, t0);
                    var t1      = t0 + fi.duration;
                    if (isExpI) f.gain.exponentialRampToValueAtTime(1.0, t1);
                    else        f.gain.linearRampToValueAtTime     (1.0, t1);
                }
                var fo  = sound.fadeout;
                if (fo.duration > 0 && isFinite(dur)) {
                    var isExpO  = fo.type == "exponential";
                    var lowO    = isExpO ? rc.dbamp(-60) : 0.0;
                    var t3      = t0 + dur;
                    var t2      = t3 - fo.duration;
                    f.gain.setValueAtTime(1.0, t2);
                    if (isExpO) f.gain.exponentialRampToValueAtTime(lowO, t3);
                    else        f.gain.linearRampToValueAtTime     (lowO, t3);
                } else if (stop < totalDur) {
                    var t4      = t0 + dur;
                    f.gain.setValueAtTime(0.0, t4);
                }
                self._addTail(f);
            }
            self._connect(self._outNode, context.destination);
            self._connected = true;
            $(m.elem()).trigger("connected");
        }
        m.play();
        if (dur < totalDur) {
            // if we use the envelope and no loop, schedule a bit later
            var dly = sound.loop || !self._needsFade ? dur : dur + 0.1;
            self._schedule(self._ended, dly);
        }
    };

    /* A function that is invoked either when the timer
     * for the region stop expires or the overall end
     * of the sound file is reached. This checks if
     * looping is active and in that case re-starts
     * playing from the start.
     */
    self._ended = function() {
        rc.log("ended " + sound.src);
        var repeat = self._playing && !self._releasing;
        self.stop();
        if (sound.loop && repeat) self.play();
    };

    /* A function that listens for events from
     * the media element. It issues `doPlay` if
     * the logical playing status is `true` and
     * the element has gone into `canplay` state.
     * It also forwards the `ended` event and
     * issues a seek command as soon as enough
     * data is available.
     */
    self._eventFun = function(e) {
        var m = self.model();
        if (e.type == "loadedmetadata" && m.paused()) {
            // console.log("loadedmetadata - readyState is " + m.readyState());
            if (sound.start > 0) {
                m.currentTime(sound.start);
            }
        } else if (e.type == "canplay" && m.paused()) {
            // console.log("canplay - readyState is " + m.readyState());
            if (self._playing) self._doPlay();
        } else if (e.type == "ended") {
            self._ended();
        }
        // console.log(e.type);
    };

    /* Implementation of `stop()`. Clears the list
     * of scheduled functions, pauses the audio element
     * and disconnects the audio graph.
     * Cues back to sound's start position
     */
    self._stop1 = function(seek) {
        self._playing   = false;
        self._releasing = false;
        self._clearSchedule();
        var m = self.model();
        if (!m.paused()) m.pause();
        // reset position
        if (seek) m.currentTime(sound.start);
        self._disconnectAll();
    };

    /* Implementation of `release()`.
     * Rather than fiddling around with an existing
     * gain node, it inserts a new gain node between
     * the `_outNode` and the audio context destination,
     * producing a linear fade-out over the given
     * duration. The fading node replaces `_outNode`.
     */
    self._release1 = function(dur) {
        var outNode     = self._outNode;
        var context     = outNode.context;
        var t0          = context.currentTime;
        var t1          = t0 + dur;
        var f           = context.createGain();
        f.gain.setValueAtTime           (1.0, t0);
        f.gain.linearRampToValueAtTime  (0.0, t1);
        // Ok, here's the deal. Firefox
        // only behaves correctly if we obey the
        // following order of connections and
        // disconnections. First connect the
        // fading node to output, then reconnect
        // the original tail node.
        self._connect   (f      , context.destination);
        self._disconnect(outNode, context.destination);
        self._addTail(f);
        self._releasing = true;
    };

    // finally run init
    self._init();
};