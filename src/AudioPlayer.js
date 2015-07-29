rc.AudioPlayer = function AudioPlayer() {
    if (!(this instanceof AudioPlayer)) {
        return new AudioPlayer();
    }

    var self = this;

    /* The constructor for JQuery UI. */
    self._create = function () {
        self = this;
    }
};

$.widget("rc.AudioPlayer", new rc.AudioPlayer());