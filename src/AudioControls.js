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
            var sec1 = neg ? Math.ceil(sec) : Math.floor(sec);
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
        var svgPlay = $('<svg width="26" height="26"><g transform="scale(0.8)"><path></path></g></svg>');

        // cf. http://stackoverflow.com/questions/3642035/jquerys-append-not-working-with-svg-element
        // i.e., we cannot append to SVG with JQuery. Instead we construct the whole
        // object and then use search to find the `path`.
        var updatePlay = function() {
            var d = model.playing() ?
                "m 6.6875,5.312 0,20.376 5.65625,0 0,-20.376 z m 12.3125,0 0,20.376 5.312,0 0,-20.376 z" :
                "M6.684,25.682L24.316,15.5L6.684,5.318V25.682z";
            $("path", svgPlay).attr("d", d);
        };

        updatePlay();

        divPlay.append(svgPlay);
        divPlay.click(function() {
            $(self).trigger("play");
        });
        div.append(divPlay);

        $(model).on("playing", updatePlay).on("pause", updatePlay);
    }

    if (optOpt.elapsed) {
        var divElapsed = $('<span class="rc-timer"></span>');
        div.append(divElapsed);

        var updateElapsed = function() {
            var time = model.currentTime();
            self._updateTimer(divElapsed, time);
        };

        updateElapsed();
        $(model).on("timeupdate", updateElapsed);
    }

    if (optOpt.remaining) {
        var divRemaining = $('<span class="rc-timer"></span>');
        div.append(divRemaining);

        var updateRemaining = function() {
            var time = model.currentTime();
            var dur  = model.duration();
            self._updateTimer(divRemaining, dur - time, true);
        };

        updateRemaining();
        $(model).on("timeupdate", updateRemaining).on("durationchange", updateRemaining);
    }

    if (optOpt.volume) {
        var divVolume = $('<span class="rc-volume"></span>');
        var svgVolume = $('<svg width="26" height="26"><g transform="scale(0.8)"><path></path></g></svg>');

        var regularVolume   = model.volume();
        var muted           = false;

        var updateVolume = function() {
            var volume = model.volume();
            console.log("volume = " + volume);
            var vol0 = "M4.998,12.127v7.896h4.495l6.729,5.526l0.004-18.948l-6.73,5.526H4.998z";
            var vol1 = "M18.806,11.219c-0.393-0.389-1.024-0.389-1.415,0.002c-0.39,0.391-0.39,1.024,0.002,1.416v-0.002c0.863,0.864,1.395,2.049,1.395,3.366c0,1.316-0.531,2.497-1.393,3.361c-0.394,0.389-0.394,1.022-0.002,1.415c0.195,0.195,0.451,0.293,0.707,0.293c0.257,0,0.513-0.098,0.708-0.293c1.222-1.22,1.98-2.915,1.979-4.776C20.788,14.136,20.027,12.439,18.806,11.219z";
            var vol2 = "M21.101,8.925c-0.393-0.391-1.024-0.391-1.413,0c-0.392,0.391-0.392,1.025,0,1.414c1.45,1.451,2.344,3.447,2.344,5.661c0,2.212-0.894,4.207-2.342,5.659c-0.392,0.39-0.392,1.023,0,1.414c0.195,0.195,0.451,0.293,0.708,0.293c0.256,0,0.512-0.098,0.707-0.293c1.808-1.809,2.929-4.315,2.927-7.073C24.033,13.24,22.912,10.732,21.101,8.925z";
            var vol3 = "M23.28,6.746c-0.393-0.391-1.025-0.389-1.414,0.002c-0.391,0.389-0.391,1.023,0.002,1.413h-0.002c2.009,2.009,3.248,4.773,3.248,7.839c0,3.063-1.239,5.828-3.246,7.838c-0.391,0.39-0.391,1.023,0.002,1.415c0.194,0.194,0.45,0.291,0.706,0.291s0.513-0.098,0.708-0.293c2.363-2.366,3.831-5.643,3.829-9.251C27.115,12.389,25.647,9.111,23.28,6.746z";
            var d = vol0;
            if (volume > 0) {
                d += vol1;
                if (volume > 0.5) {
                    d += vol2;
                    if (volume > 0.8) {
                        d += vol3;
                    }
                }
            }
            $("path", svgVolume).attr("d", d);
        };

        updateVolume();

        divVolume.append(svgVolume);
        divVolume.click(function() {
            if (muted) model.volume(regularVolume > 0 ? regularVolume : 1.0);
            else {
                regularVolume = model.volume();
                model.volume(0.0);
            }
            muted = !muted;
            console.log("muted = " + muted);
        });
        div.append(divVolume);

        $(model).on("volumechange", updateVolume);
    }

    $(elem).append(div);

    if (optOpt.meter) {

    }

    var canvas = $('<canvas width="160" height="20" class="rc-meter"></canvas>');
    div.append(canvas);
};