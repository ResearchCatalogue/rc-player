rc.AudioPlayer = function AudioPlayer() {
    if (!(this instanceof AudioPlayer)) {
        return new AudioPlayer();
    }

    var self = this;

    // forwarders - at some point `self._sound` might be changing

    self.playing        = function() { return self._sound.playing    () };
    self.currentTime    = function() { return self._sound.currentTime() };
    self.duration       = function() { return self._sound.duration   () };

    self._attachSound = function() {
        var sound   = self._sound;
        var audio   = sound.audioElem();

        //var printObj = function(obj) {
        //    for (var i in obj) {
        //        console.log(i);
        //    }
        //};

        // $(audio).delegate()

        $(audio)
            .on("playing", function(e) {
                // console.log("||| playing");
                // printObj(e);
                $(self).trigger("playing", e);
            })
            .on("timeupdate", function(e) {
                // console.log("||| timeupdate");
                // printObj(e);
            })
            .on("durationchange", function(e) {
                // console.log("||| durationchange");
                // printObj(e);
            })
            .on("pause", function(e) {
                // console.log("||| pause");
                // printObj(e);
                $(self).trigger("pause", e);
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