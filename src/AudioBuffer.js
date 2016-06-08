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

    self.mediaNode = function() {
        return self._mediaNode;
    };

    self.currentTime = function(x) {
        if (x == undefined) {
            return self._currentTime;
        } else {
            self._currentTime = x;
        }
    };

    self.duration = function() {
        return self._duration;
    };

    self.readyState = function() {
        return self._readyState;
    };

    self.paused = function() {
        return "NYI";
    };

    self.src = function(x) {
        if (x == undefined) {
            return self._source;
        } else {
            self._source = x;
        }
    };

    self.load = function() {
        alert("load");
    };

    self.play = function() {
        alert("play");
    };

    self.pause = function() {
        alert("pause");
    };

    self.preload = function(x) {
        if (x == undefined) {
            return self._preload;
        } else {
            self._preload = x;
        }
    };

    // ---------------- private ----------------

    /* Initialization. This must only be called from the
     * very bottom of the constructor after all other
     * functions have been defined!
     */
    self._init = function() {
        self._currentTime   = 0.0;
        self._readyState    = 0;
        self._duration      = undefined;
        self._preload       = "none";
        self._mediaNode     = rc.AudioContext().createBufferSource();
    };

    // finally run init
    self._init();
};