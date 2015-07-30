rc.AudioPlayer = function AudioPlayer(self) {
    if (!(this instanceof AudioPlayer)) {
        return new AudioPlayer(self);
    }

    // console.log("INIT " + self.options.sound.src);

    // var self = this;

    // forwarders - at some point `self._sound` might be changing

    self.playing        = function()  { return self._sound.playing    ()  };
    self.currentTime    = function(x) { return self._sound.currentTime(x) };
    self.duration       = function()  { return self._sound.duration   ()  };
    self.volume         = function(x) { return self._sound.volume     (x) };
    self.mediaNode      = function()  { return self._sound.mediaNode  ()  };

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
            })
            .on("connected", function(e) {
                $(self).trigger("connected", e);
            })
            .on("disconnected", function(e) {
                $(self).trigger("disconnected", e);
            });
    };

    self._init1 = function () {
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
    };

    // _init is already called by JQuery UI
    self._init1();
};

rc.AudioPlayerPlugIn = {
    _create: function() {
        // console.log("CREATE " + this.options.sound.src);
        /* this._instance = */ new rc.AudioPlayer(this);
    }
};

$.widget("rc.AudioPlayer", rc.AudioPlayerPlugIn);