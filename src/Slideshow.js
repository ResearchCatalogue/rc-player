/* Slideshow.js
 * (rc-player)
 */

/**
 * The Slideshow class is instantiated by the SlideshowPlugIn widget.
 */
rc.Slideshow = function Slideshow(self) {
    if (!(this instanceof Slideshow)) {
        return new Slideshow(self);
    }

    self._init1 = function () {
        self = this;    // Yo dawg, I put a this in the self, so you can this while you self

        var div = $('<div class="rc-slideshow">');
        self._div = div;
        var opt = self.options;

        if (opt.style) rc.style(div, opt.style);

        //// XXX TODO -- perhaps create all elements
        //// already, so browser caches future images
        //var img = $('<img class="rc-slide">');
        //div.append(img);
        //self._img = img;
        self._slideIdx = 0;

        if (!opt.options) opt.options = {};
        var optOpt = opt.options;
        if (!optOpt.placement) optOpt.placement= {};
        if (!optOpt.settings ) optOpt.settings = {};
        if (!optOpt.automate ) optOpt.automate = {};
        if (!optOpt.audio    ) optOpt.audio    = {};

        self._sounds = [];
        self._images = [];

        var slides      = opt.slides;
        var numSlides   = slides.length;

        for (var idx = 0; idx < numSlides; idx++) {
            // rc.log("mkSlide(" + idx + ")");
            self._mkSlide(idx);
        }

        self.setSlide();

        var autoplay = optOpt.automate.autoplay;

        div.click(function() {
            self.nextSlide();
            if (autoplay == 'click' && !self._playing) self.play();
        });

        if (autoplay == 'on') {
            self.play();
        }

        $(self.element).replaceWith(div);
    };

    self._showSlide = function(idx) {
        if (self._currentImage) {
            self._currentImage.image.css("display", "none");
        }
        self._currentImage = self._images[idx];
        if (self._currentImage.ready) {
            self._currentImage.image.css("display", "inline"); // or block?
        }
    };

    self._mkSlide = function(idx) {
        var opt     = self.options;
        var optOpt  = opt.options;
        var place   = optOpt.placement;
        var slides  = opt.slides;
        var slide   = slides[idx];
        // console.log(slide.sound !== undefined);
        var img     = $('<img class="rc-slide">');
        img.css("display", "none");
        img.attr("src", slide.image);
        var imgDOM  = img.get(0);
        var imgInfo = { image: img, ready: false };
        var loadFun = function() {
            var nw  = Math.max(1, imgDOM.naturalWidth);
            var nh  = Math.max(1, imgDOM.naturalHeight);
            var cw  = self._div.width ();
            var ch  = self._div.height();
            var sx  = cw / nw;
            var sy  = ch / nh;
            var iw  = 0;
            var ih  = 0;

            // `contain`, `cover`, `fit`, `auto`
            switch (place.size) {
                case "contain":
                default:
                    var scale1 = Math.min(sx, sy);
                    iw = Math.floor(scale1 * nw);
                    ih = Math.floor(scale1 * nh);
                    break;
                case "cover":
                    var scale2 = Math.max(sx, sy);
                    iw = Math.floor(scale2 * nw);
                    ih = Math.floor(scale2 * nh);
                    break;
                case "fit":
                    iw = cw;
                    ih = ch;
                    break;
                case "auto":
                    iw = nw;
                    ih = nh;
                    break;
            }
            img.width (iw);
            img.height(ih);
            var x       = 0;
            var y       = 0;
            var pos     = place.position ? place.position : "center-center";
            var posI    = pos.indexOf("-");

            switch (pos.substring(0, posI)) {
                case "left":
                    x = 0;
                    break;
                case "center":
                default:
                    x = Math.round((cw - iw) / 2);
                    break;
                case "right":
                    x = cw - iw;
                    break;
            }
            switch (pos.substring(posI + 1)) {
                case "top":
                    y = 0;
                    break;
                case "center":
                default:
                    y = Math.round((ch - ih) / 2);
                    break;
                case "bottom":
                    y = ch - ih;
                    break;
            }
            img.css("margin-left", x);
            img.css("margin-top" , y);

            // console.log("" + idx + ": cw = " + cw + "; ch = " + ch + "; nw = " + nw + "; nh = " + nh + "; sx = " + sx + "; sy = " + sy);
            // console.log("" + idx + ": x = " + x + "; y = " + y + "; w = " + iw + "; h = " + ih);

            imgInfo.ready = true;
            if (self._slideIdx == idx) self._showSlide(idx);
        };
        img.on('load', loadFun);
        img.load(loadFun);
        self._images[idx] = imgInfo;
        if (slide.sound) self._sounds[idx] = rc.AudioRegion(slide.sound);
        // console.log(this._sounds[idx]);
        self._div.append(img);
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
        // var url     = slide.image;
        // console.log("setting " + url);
        // self._img.attr("src", url);
        self._showSlide(idx);
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
    };

    rc.log("begin init Slideshow");
    self._init1();
    rc.log("end init Slideshow");
};

rc.SlideshowPlugIn = {
    _create: function() {
        // console.log("CREATE " + this.options.sound.src);
        this._instance = new rc.Slideshow(this);
    },

    options: {
        style: {
            background: {
                color: "black"
            }
        }
    }
};

$.widget("rc.Slideshow", rc.SlideshowPlugIn);