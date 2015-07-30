rc.AudioControls = function AudioControls(options) {
    if (!(this instanceof AudioControls)) {
        return new AudioControls(options);
    }

    var self = this;

    var div = $('<div class="rc-audio-controls"></div>');

    var optOpt = options.options;

    //for (var x in optOpt) {
    //    console.log(x);
    //}

    var model   = options.model;
    var elem    = model.element;
    var width   = elem.width();
    var height  = elem.height();

    // console.log("width = " + width + "; height = " + height);

    self._updateTimer = function(elem, sec, neg) {
        var txt = "";
        if (isFinite(sec)) {
            var sec1 = Math.floor(sec);
            var min0 = Math.floor(sec1 / 60);
            var sec2 = sec1 % 60;
            var min1 = Math.floor(min0 / 10) % 10;
            var min2 = min0 % 10;
            var sec3 = Math.floor(sec2 / 10);
            var sec4 = sec2 % 10;
            txt = (min1 == 0 ? (neg ? "\xa0-" : "\xa0") : (neg ? "-" : "") + min1) +
                min2 + ":" + sec3 + sec4;
        }
        elem.text(txt);
    };

    if (optOpt.play) {
        var divPlay = $('<span class="rc-play"></span>');
        var svgPlay = $('<svg width="26" height="26"><g transform="scale(0.8)">' +
            '<path class="rc-playing" d="M6.684,25.682L24.316,15.5L6.684,5.318V25.682z"></path>' +
            '<path class="rc-pausing" d="m 6.6875,5.312 0,20.376 5.65625,0 0,-20.376 z m 12.3125,0 0,20.376 5.312,0 0,-20.376 z"></path>' +
            '</g></svg>');
        divPlay.addClass(model.playing() ? "rc-playing" : "rc-pausing");
        // svgPlay.css("fill", "white");
        divPlay.append(svgPlay);
        // divPlay.css("background", "black");
        divPlay.click(function() {
            // console.log("click");
            // var sound = options.sound;
            // if (sound.playing()) sound.pause(); else sound.play();
            $(self).trigger("play");
        });
        div.append(divPlay);

        $(model)
            .on("playing", function() {
                // console.log("||| playing");
                divPlay.addClass("rc-playing").removeClass("rc-pausing");
            })
            .on("pause", function() {
                // console.log("||| pause");
                divPlay.removeClass("rc-playing").addClass("rc-pausing");
            });
    }

    if (optOpt.elapsed) {
        var divElapsed = $('<span class="rc-timer"></span>');
        div.append(divElapsed);

        self._updateTimer(divElapsed, model.currentTime());

        $(model)
            .on("timeupdate", function() {
                var time = model.currentTime();
                // console.log("||| timeupdate " + time);
                self._updateTimer(divElapsed, time);
            });
    }

    if (optOpt.remaining) {
        var divRemaining = $('<span class="rc-timer">&nbsp;-0:00</span>');
        div.append(divRemaining);
    }

    if (optOpt.volume) {
        var divVolume = $('<span class="rc-volume"></span>');
        var svgVolume = $('<svg width="32" height="32"><path d="M6.684,25.682L24.316,15.5L6.684,5.318V25.682z"></path></svg>');
        // svgPlay.css("fill", "white");
        divVolume.append(svgVolume);
        // divPlay.css("background", "black");
        divVolume.click(function() {
        });
        div.append(divVolume);
    }

    $(elem).append(div);

    if (optOpt.meter) {

    }

    var canvas = $('<canvas width="160" height="20" class="rc-meter"></canvas>');
    div.append(canvas);
};