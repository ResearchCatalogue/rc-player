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

    if (optOpt.play) {
        var divPlay = $('<div class="rc-play"></div>');
        var svgPlay = $('<svg width="32" height="32"><path d="M6.684,25.682L24.316,15.5L6.684,5.318V25.682z"></path></svg>');
        svgPlay.css("fill", "white");
        divPlay.append(svgPlay);
        div.append(divPlay);
        divPlay.css("background", "black");
        divPlay.click(function() {
            // console.log("click");
            var sound = options.sound;
            if (sound.playing()) sound.pause(); else sound.play();
        });
    }

    $(options.element).append(div);

    self._init = function() {

    };

    self._init();
};