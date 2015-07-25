$.widget("rc.Slideshow", {
    _create: function () {
        var div = $('<div class="rc-slideshow">');
        div.css("left"  , this.options.left  );
        div.css("top"   , this.options.top   );
        div.css("width" , this.options.width );
        div.css("height", this.options.height);

        // TODO: padding, border, etc. goes here

        var img = $('<img class="rc-slide">')
        img.attr("src", this.options.slides[0].url);
        var self = this;
        img.click(function() {
            self.nextSlide();
        });
        div.append(img);
        this._img = img;
        this._slideIdx = 0;
        $(this.element).replaceWith(div);
    },

    nextSlide: function() {
        var slides      = this.options.slides;
        var numSlides   = slides.length;
        var idx         = this._slideIdx + 1;
        if (idx < numSlides || this.options.loop) {
            idx = idx % numSlides;
            this._slideIdx  = idx;
            this._img.attr("src", slides[idx].url);
            // $.trigger()
        }
    }
});