$.widget("rc.Slideshow", function() {
    var self;

    return {
        _create: function () {
            self = this;

            var div = $('<div class="rc-slideshow">');
            var opt = self.options;

            div.css("left"  , opt.left  );
            div.css("top"   , opt.top   );
            div.css("width" , opt.width );
            div.css("height", opt.height);

            // TODO: padding, border, etc. goes here

            var img = $('<img class="rc-slide">')
            img.attr("src", opt.slides[0].url);
            div.append(img);
            self._img = img;
            self._slideIdx = 0;

            self.setSlide();

            img.click(function() {
                self.nextSlide();
            });

            if (opt.autoPlay == 'on') {
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
            if (nextIdx < numSlides || opt.loop) {
                var slide   = slides[idx];
                var delay   = slide.duration;
                if (delay == undefined) delay = opt.duration;
                if (delay == undefined) delay = 4.0;
                window.setTimeout(self.nextSlide, delay * 1000);
            }
        },

        nextSlide: function() {
            var opt         = self.options;
            var slides      = opt.slides;
            var numSlides   = slides.length;
            var idx         = self._slideIdx + 1;
            if (idx < numSlides || opt.loop) {
                idx = idx % numSlides;
                self._slideIdx  = idx;
                self.setSlide();
                if (opt.autoPlay == 'on') self.play();
            }
        },

        setSlide: function() {
            var idx     = self._slideIdx;
            var opt     = self.options;
            var slides  = opt.slides;
            self._img.attr("src", slides[idx].url);
            // $.trigger()
        }
    }
}());