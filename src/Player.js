var rc = {
    dbamp: function(x) {
        return Math.pow(10, x * 0.05);
    },

    AudioRegion: function AudioRegion(sound) {
        if (!(this instanceof AudioRegion)) { // bloody hell, this language is the worst abomination ever
            return new AudioRegion(sound);
        }

        var self = this;    // bloody hell, this language is the worst abomination ever

        var audio       = document.createElement("AUDIO");
        self._elem      = audio;
        self._events    = ["loadedmetadata", "canplay", "durationchange", "emptied", "ended", "error",
                            "loadstart", "progress", "timeupdate"];

        var needsWA = sound.fadein || sound.fadeout || (sound.gain && sound.gain > 0);
        if (needsWA ) {
            self._mediaSource = AudioContext.createMediaElementSource(audio);
            self._connected = false;
        }

        self._doPlay = function() {
            var audio = self._elem;
            audio.play();
        }

        self._eventFun  = function(e) {
            var audio = self._elem;
            if (e.type == "loadedmetadata" && audio.paused) {
                // console.log("loadedmetadata - readyState is " + audio.readyState);
                if (sound.start && sound.start != 0) {
                    audio.currentTime = sound.start;
                }
            } else if (e.type == "canplay" && audio.paused) {
                // console.log("canplay - readyState is " + audio.readyState);
                if (self._playing) self._doPlay();
            };
            console.log(e.type);
        }

        for (var i = 0; i < self._events.length; i++) {
            audio.addEventListener(self._events[i], self._eventFun);
        };

        audio.preload   = "auto";
        audio.src       = sound.src;
        if (sound.gain && sound.gain < 0) { // N.B. only attenuation supported here
            audio.volume = rc.dbamp(sound.gain);
        }
 
        self.play = function() {
            self._playing = true;
            var audio = self._elem;
            if (audio.readyState >= 2) self._doPlay();
        }

        self.stop = function() {
            self._playing = false;
            var audio = self._elem;
            audio.pause();
        }

        self.dispose = function() {
            var audio = self._elem;
            for (var i = 0; i < self._events.length; i++) {
                audio.removeEventListener(self._events[i], self._eventFun);
            };
            self.stop();
            audio.src = "";
            audio.load();
        }
    }
}