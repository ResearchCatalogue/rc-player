rc.AudioPlayer = function AudioPlayer() {
    if (!(this instanceof AudioPlayer)) {
        return new AudioPlayer();
    }

    var self = this;

    /* The constructor for JQuery UI. */
    self._create = function () {
        self = this;

        // var img = $('<img class="rc-slide">');
        // div.append(img);
        var opt     = self.options;
        var optOpt  = opt.options;
        var ctl     = optOpt.controls;
        if (!ctl) ctl = {};

        var ctlOpt = {
            element: self.element,
            options: ctl,
            style  : opt.style
        };
        /* var ggCtl = */ rc.AudioControls(ctlOpt);

        self._audio = rc.AudioRegion(opt.sound);
        if (optOpt.autoplay) self._audio.play();
    }
};

$.widget("rc.AudioPlayer", new rc.AudioPlayer());