$.widget("rc.Slideshow", function() {
    var self;

    return {
        _create: function () {
            self = this;

            var div = $('<div class="rc-slideshow">');
            var opt = self.options;

            if (opt.style) {
                var style = opt.style
                if (style.position) {
                    var pos = style.position
                    div.css("left"  , pos.left  );
                    div.css("top"   , pos.top   );
                    div.css("width" , pos.width );
                    div.css("height", pos.height);
                }
                // TODO: padding, border, etc. goes here
            }

            var img = $('<img class="rc-slide">');
            div.append(img);
            self._img = img;
            self._slideIdx = 0;

            if (!opt.options) opt.options = {};
            var optOpt = opt.options;
            if (!optOpt.settings) optOpt.settings = {};
            if (!optOpt.automate) optOpt.automate = {};
            if (!optOpt.audio   ) optOpt.audio    = {};

            this._sounds = [];

            var slides      = opt.slides;
            var numSlides   = slides.length;
            for (var i = 0; i < numSlides; i++) {
                var slide = slides[i];
                // console.log(slide.sound !== undefined);
                if (slide.sound) this._sounds[i] = rc.AudioRegion(slide.sound);
                // console.log(this._sounds[i]);
            }

            self.setSlide();

            img.click(function() {
                self.nextSlide();
            });

            if (optOpt.automate.autoplay == 'on') {
                self.play()
            };

            $(self.element).replaceWith(div);
        },

        play: function() {
            var opt         = self.options;
            var slides      = opt.slides;
            var numSlides   = slides.length;
            var idx         = self._slideIdx;
            var nextIdx     = idx + 1;
            var optOpt      = opt.options;
            if (nextIdx < numSlides || optOpt.settings.loop) {
                var slide   = slides[idx];
                var delay   = slide.duration;
                if (delay == undefined) delay = optOpt.automate.duration;
                if (delay == undefined) delay = 4.0;
                window.setTimeout(self.nextSlide, delay * 1000);
            }
        },

        nextSlide: function() {
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
        },

        setSlide: function() {
            var idx     = self._slideIdx;
            var opt     = self.options;
            var slides  = opt.slides;
            var url     = slides[idx].image;
            // console.log("setting " + url);
            self._img.attr("src", url);
            var sound   = this._sounds[idx];
            if (sound) sound.play();
            // $.trigger()
        }
    }
}());