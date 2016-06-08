/* AudioBuffer.js
 * (rc-player)
 */

/**
 *  A class buffering an audio file and mimicking
 *  the API of an HTML <audio> element.
 */
rc.AudioBuffer = function AudioBuffer() {
    if (!(this instanceof AudioBuffer)) {
        return new AudioBuffer();
    }

    var self = this;

    self.elem = function() {
        return self._elem;
    };

    self.mediaNode = function() {
        return self._mediaNode;
    };

    self._elapsed = function() {
        if (self.paused()) {
            return 0.0;
        } else {
            var t0 = self._playTime;
            var t1 = self.mediaNode().context.currentTime;
            return t1 - t0;
        }
    };

    self.currentTime = function(x) {
        if (x == undefined) {
            return self._startTime + self._elapsed();
        } else {
            self._startTime = x;
        }
    };

    self.duration = function() {
        return self._duration;
    };

    self.readyState = function() {
        return self._readyState;
    };

    self.paused = function() {
        return self._paused;
    };

    self.src = function(x) {
        if (x == undefined) {
            return self._source;
        } else {
            if (self._source != x) {
                self._source = x;
                if (self.preload() != "none") self.load();
            }
        }
    };

    self._abortReq = function() {
      if (self._req) {
          self._req.abort();
          self._req = null;
      }
    };

    self._reqLoaded = function(req) {
        var ctx = rc.AudioContext();
        rc.log("reqLoaded " + self.src());
        ctx.decodeAudioData(
            req.response,
            function(buf) {
                if (self._req == req) self._reqDecoded(req, buf);
            },
            function(e) {
                if (self._req == req)
                    console.error("ERROR: AudioBuffer - " + self.src() + "  - context.decodeAudioData:", e);
            }
        );
    };

    self._reqDecoded = function(req, buf) {
        rc.log("reqDecoded " + self.src());
        self._buf = buf;
        self._duration = buf.duration;
        var m = self.elem();
        m.dispatchEvent(new Event("durationchange"));
        m.dispatchEvent(new Event("loadedmetadata"));
        m.dispatchEvent(new Event("canplay"));
    };

    self.load = function() {
        var url = self.src();
        self._abortReq();
        if (url == "") {
            // XXX
        } else {
            var req = new XMLHttpRequest();
            self._req = req;
            req.open("GET", url, true);
            req.responseType = "arraybuffer";
            req.addEventListener("load", function() {
                if (self._req == req) self._reqLoaded(req);
            });
            req.send();
        }
    };

    self._freeSource = function() {
        var n = self._sourceNode;
        if (n) {
            n.stop();
            n.disconnect(self.mediaNode());
            self._sourceNode = null;
            self._paused     = true;
        }
        if (self._timerID) {
            clearInterval(self._timerID);
            self._timerID = null;
        }
    };

    self._timerUpdate = function() {
        var m = self.elem();
        m.dispatchEvent(new Event("timeupdate"));
    };

    self.play = function() {
        self._freeSource();
        rc.log("AudioBuffer play()");
        self._paused    = false;
        var gain        = self.mediaNode();
        var context     = gain.context;
        var source      = context.createBufferSource();
        source.buffer   = self._buf;
        source.connect(gain);
        var t0          = context.currentTime;
        self._playTime  = t0;
        source.start(t0, self._startTime);
        var m = self.elem();
        m.dispatchEvent(new Event("playing"));
        self._timerID = setInterval(self._timerUpdate, 100);
    };

    self.pause = function() {
        rc.log("AudioBuffer pause()");
        self._freeSource();
        var m = self.elem();
        m.dispatchEvent(new Event("pause"));
    };

    self.preload = function(x) {
        if (x == undefined) {
            return self._preload;
        } else {
            if (self._preload != x) {
                var wasNone = self._preload == "none";
                self._preload = x;
                if (wasNone) self.load();
            }
        }
    };

    // ---------------- private ----------------

    /* Initialization. This must only be called from the
     * very bottom of the constructor after all other
     * functions have been defined!
     */
    self._init = function() {
        var m = document.createElement("SPAN");
        self._elem          = m;
        self._source        = "";
        self._paused        = true;
        self._startTime     = 0.0;
        self._playTime      = 0.0;
        self._readyState    = 0;
        self._duration      = undefined;
        self._preload       = "none";
        self._mediaNode     = rc.AudioContext().createGain();
    };

    // finally run init
    self._init();
};