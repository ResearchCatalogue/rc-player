/* AudioPlayer.js
 * (rc-player)
 */

/**
 * The AudioPlayer class is instantiated by the AudioPlayerPlugIn
 * widget. It stores an AudioRegion object that it controls.
 */
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
    self.pause          = function()  { return self._sound.pause      ()  };
    self.stop           = function()  { return self._sound.stop       ()  };

    self.play = function() {
        var optOpt = self.options.options;
        if (optOpt.stopothers) {
            $(":rc-AudioPlayer").each(function(idx, elem) {
                var obj = $(elem).data("rc-AudioPlayer");
                if (obj != self) {
                    obj.stop();
                }
            });
        }
        self._sound.play();
    };

    self._attachSound = function() {
        var sound   = self._sound;
        var audio   = sound.audioElem();

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
                // this is a WTF:
                // if we call this "pause", JQuery
                // will actually call the pause()
                // function.
                $(self).trigger("paused", e);
            })
            .on("ended", function(e) {
                $(self).trigger("ended", e);
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
        /* var ggCtl = */ rc.AudioControls(ctlOpt);

        self._attachSound();

        if (optOpt.autoplay) self._sound.play();
    };

    // _init is already called by JQuery UI
    self._init1();
};

rc.AudioPlayerPlugIn = {
    _create: function() {
        // console.log("CREATE " + this.options.sound.src);
        this._instance = new rc.AudioPlayer(this);
    },

    options: {
        style: {
            background: {
                color: "black"
            }
        }
    }
};

$.widget("rc.AudioPlayer", rc.AudioPlayerPlugIn);