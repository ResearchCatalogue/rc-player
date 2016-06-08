/* AudioStream.js
 * (rc-player)
 */

/**
 *  A class streaming an audio file and mimicking
 *  the API of an HTML <audio> element.
 */
rc.AudioStream = function AudioStream() {
    if (!(this instanceof AudioStream)) {
        return new AudioStream();
    }

    var self = this;

    self.elem = function() {
        return self._elem;
    };

    self.mediaNode = function() {
        return self._mediaNode;
    };

    self.currentTime = function(x) {
        var m = self.elem();
        if (x == undefined) {
            return m.currentTime;
        } else {
            rc.log("<S> currentTime = " + x);
            m.currentTime = x;
        }
    };

    self.duration = function() {
        var m = self.elem();
        return m.duration;
    };

    self.readyState = function() {
        var m = self.elem();
        return m.readyState;
    };

    self.paused = function() {
        var m = self.elem();
        return m.paused;
    };

    self.src = function(x) {
        var m = self.elem();
        if (x == undefined) {
            return m.src;
        } else {
            rc.log("<S> src = " + x);
            m.src = x;
        }
    };

    self.load = function() {
        var m = self.elem();
        m.load();
    };

    self.play = function() {
        rc.log("<S> play()");
        var m = self.elem();
        m.play();
    };

    self.pause = function() {
        rc.log("<S> pause()");
        var m = self.elem();
        m.pause();
    };

    self.preload = function(x) {
        var m = self.elem();
        if (x == undefined) {
            return m.preload;
        } else {
            rc.log("<S> preload = " + x);
            m.preload = x;
        }
    };

    // ---------------- private ----------------

    /* Initialization. This must only be called from the
     * very bottom of the constructor after all other
     * functions have been defined!
     */
    self._init = function() {
        var m = document.createElement("AUDIO");
        self._elem          = m;
        self._mediaNode     = rc.AudioContext().createMediaElementSource(m);
        m.setAttribute('crossorigin', 'anonymous'); // CORS
    };

    // finally run init
    self._init();
};