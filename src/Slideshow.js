rc.Slideshow = function Slideshow() {
    if (!(this instanceof Slideshow)) {
        return new Slideshow();
    }

    var self = this;

    /* The constructor for JQuery UI. */
    self._create = function () {
        self = this;    // Yo dawg, I put a this in the self, so you can this while you self

        var div = $('<div class="rc-slideshow">');
        var opt = self.options;

        if (opt.style) rc.style(div, opt.style);

        // XXX TODO -- perhaps create all elements
        // already, so browser caches future images
        var img = $('<img class="rc-slide">');
        div.append(img);
        self._img = img;
        self._slideIdx = 0;

        if (!opt.options) opt.options = {};
        var optOpt = opt.options;
        if (!optOpt.settings) optOpt.settings = {};
        if (!optOpt.automate) optOpt.automate = {};
        if (!optOpt.audio   ) optOpt.audio    = {};

        self._sounds = [];

        var slides      = opt.slides;
        var numSlides   = slides.length;
        for (var idx = 0; idx < numSlides; idx++) {
            var slide = slides[idx];
            // console.log(slide.sound !== undefined);
            if (slide.sound) self._sounds[idx] = rc.AudioRegion(slide.sound);
            // console.log(this._sounds[idx]);
        }

        self.setSlide();

        var autoplay = optOpt.automate.autoplay;

        img.click(function() {
            self.nextSlide();
            if (autoplay == 'click' && !self._playing) self.play();
        });

        if (autoplay == 'on') {
            self.play()
        }

        $(self.element).replaceWith(div);
    };

    self._playing = false;

    self.playing = function() { return self._playing };

    self.play = function() {
        var opt         = self.options;
        var slides      = opt.slides;
        var numSlides   = slides.length;
        var idx         = self._slideIdx;
        var nextIdx     = idx + 1;
        var optOpt      = opt.options;
        if (nextIdx < numSlides || optOpt.settings.loop) {
            self._playing = true;
            var slide   = slides[idx];
            var delay   = slide.duration;
            if (delay == undefined) delay = optOpt.automate.duration;
            if (delay == undefined) delay = 4.0;
            self._timeOut = window.setTimeout(self.nextSlide, delay * 1000);
        }
    };

    self.stop = function() {
        if (self._playing) {
            self._playing = false;
            window.clearTimeout(self._timeOut);
        }
    };

    self.nextSlide = function() {
        var opt         = self.options;
        var slides      = opt.slides;
        var numSlides   = slides.length;
        var idx         = self._slideIdx + 1;
        var optOpt      = opt.options;
        if (idx < numSlides || optOpt.settings.loop) {
            idx = idx % numSlides;
            self._slideIdx  = idx;
            self.setSlide();
            if (optOpt.automate.autoplay == 'on') self.play();
        }
    };

    self.setSlide = function() {
        var idx     = self._slideIdx;
        var opt     = self.options;
        var slides  = opt.slides;
        var slide   = slides[idx];
        var url     = slide.image;
        // console.log("setting " + url);
        self._img.attr("src", url);
        var sound   = self._sounds[idx];
        if (sound || slide.sound == "none") {
            var curr    = self._currentSound;
            var optOpt  = opt.options;
            var x       = optOpt.audio.crossfade;
            if (curr) {
                if (x) {
                    curr.release(x);
                } else {
                    curr.stop();
                }
                self._currentSound = null;
            }
            if (sound) {
                if (sound.playing()) {
                    // console.log("new sound is already playing.");
                    if (x) {
                        // console.log("...release and dispose " + x);
                        sound.releaseAndDispose(x);
                        sound = rc.AudioRegion(slide.sound);
                        self._sounds[idx] = sound;
                    } else {
                        // console.log("...stop.");
                        sound.stop();
                    }
                }
                sound.play();
                self._currentSound = sound;
            }
        }
        // $.trigger()
    }
};

$.widget("rc.Slideshow", new rc.Slideshow());