rc.AudioPlayer = function AudioPlayer() {
    if (!(this instanceof AudioPlayer)) {
        return new AudioPlayer();
    }

    var self = this;

    // forwarders - at some point `self._sound` might be changing

    self.playing        = function()  { return self._sound.playing    () };
    self.currentTime    = function()  { return self._sound.currentTime() };
    self.duration       = function()  { return self._sound.duration   () };
    self.volume         = function(x) { return self._sound.volume    (x) };

    self._attachSound = function() {
        var sound   = self._sound;
        var audio   = sound.audioElem();

        //var printObj = function(obj) {
        //    for (var i in obj) {
        //        console.log(i);
        //    }
        //};

        // $(audio).delegate()

        // XXX TODO -- there must be a more efficient way to create forwarding
        $(audio)
            .on("playing", function(e) {
                $(self).trigger("playing", e);
            })
            .on("timeupdate", function(e) {
                $(self).trigger("timeupdate", e);
            })
            .on("durationchange", function(e) {
                $(self).trigger("durationchange", e);
            })
            .on("pause", function(e) {
                $(self).trigger("pause", e);
            })
            .on("volumechange", function(e) {
                $(self).trigger("volumechange", e);
            });
    };

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
            model   : self,
            options : ctl,
            style   : opt.style,
            sound   : self._sound
        };
        var ggCtl = rc.AudioControls(ctlOpt);
        $(ggCtl).on("play", function() {
            var sound = self._sound;
            if (sound.playing()) sound.pause(); else sound.play();
        });

        self._attachSound();

        if (optOpt.autoplay) self._sound.play();
    }
};

$.widget("rc.AudioPlayer", new rc.AudioPlayer());