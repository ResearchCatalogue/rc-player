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

        if (opt.style) rc.style(self.element, opt.style);

        self._sound = rc.AudioRegion(opt.sound);

        var ctlOpt = {
            element : self.element,
            options : ctl,
            style   : opt.style,
            sound   : self._sound
        };
        /* var ggCtl = */ rc.AudioControls(ctlOpt);

        if (optOpt.autoplay) self._sound.play();
    }
};

$.widget("rc.AudioPlayer", new rc.AudioPlayer());