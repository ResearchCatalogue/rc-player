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

    var elem    = options.element;
    var width   = elem.width();
    var height  = elem.height();

    // console.log("width = " + width + "; height = " + height);

    if (optOpt.play) {
        var divPlay = $('<span class="rc-play"></span>');
        var svgPlay = $('<svg width="32" height="32"><path d="M6.684,25.682L24.316,15.5L6.684,5.318V25.682z"></path></svg>');
        // svgPlay.css("fill", "white");
        divPlay.append(svgPlay);
        // divPlay.css("background", "black");
        divPlay.click(function() {
            // console.log("click");
            var sound = options.sound;
            if (sound.playing()) sound.pause(); else sound.play();
        });
        div.append(divPlay);
    }

    if (optOpt.elapsed) {
        var divElapsed = $('<span class="rc-timer">&nbsp;0:00</span>');
        div.append(divElapsed);
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

    $(options.element).append(div);

    if (optOpt.meter) {

    }

    self._init = function() {
        var canvas = $('<canvas width="160" height="20" class="rc-meter"></canvas>');
        div.append(canvas);
    };

    self._init();
};