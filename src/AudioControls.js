/*

 */
rc.AudioControls = function AudioControls(options) {
    if (!(this instanceof AudioControls)) {
        return new AudioControls(options);
    }

    var self = this;

    var div     = $('<div class="rc-audio-controls"></div>');
    var div1    = div;

    var optOpt  = options.options;
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

    var bg = options.style ? options.style.background ? options.style.background.color
            : undefined : undefined;
    bg = bg ? bg : "black";

    // calculate minimum width in order
    // to determine if scale reduction is needed
    var minWidth = 0;
    var optPos   = options.style ? options.style.position ? options.style.position : {} : {};
    var optPad   = options.style ? options.style.padding  ? options.style.padding  : {} : {};
    var hPad     = 0; // optPad.left ? optPad.left : 0;
    if (optOpt.play) minWidth += 26;
    if (optOpt.elapsed) {
        hPad = Math.max(hPad, 4);
        minWidth += 36 + hPad;
        hPad = 4;
    }
    if (optOpt.cue) {
        // hPad = Math.max(hPad, 4);
        minWidth += 32 + hPad;
        hPad = 2; // 4;
    }
    if (optOpt.remaining) {
        hPad = Math.max(hPad, 4);
        minWidth += 36 + hPad;
        hPad = 4;
    }
    if (optOpt.volume) {
        minWidth += 26 + hPad;
        hPad = 0; // optPad.right ? optPad.right : 0;
    }
    minWidth += hPad;
    var minHeight = 26;
    var actualWidth     = optPos.width  ? optPos.width  : minWidth;
    var actualHeight    = optPos.height ? optPos.height : minHeight;
    var scale = Math.min(1.0, Math.min(actualWidth / minWidth, actualHeight / minHeight));
    // console.log("minWidth = " + minWidth + "; actualWidth = " + actualWidth + "; scale = " + scale);

    // crappy jQuery cannot append to svg, so we create all elements as empty here
    var svg = $('<svg width="' + actualWidth + '" height="' + Math.min(minHeight, actualHeight) + '">' +
        '<g class="rc-scale">' +
        '<g class="rc-play"><path></path><rect class="rc-button"></rect></g>' +
        '<text class="rc-timer rc-elapsed"></text>' +
        '<g class="rc-cue"><rect></rect><circle></circle></g>' +
        '<text class="rc-timer rc-remaining"></text>' +
        '<g class="rc-volume"><path></path><rect class="rc-button"></rect></g>' +
        '</g></svg>');

    if (scale < 1) {
        // N.B. unlike Firefox, Chrome doesn't scale outer 'svg' element, only 'g' elements.
        $(".rc-scale", svg).attr("transform", 'scale(' + scale + ')');
        minWidth *= scale;
    }

    div.append(svg);

    ////////////////////////////////////////////////// play

    var currentX = 0;
    hPad = 0;

    if (optOpt.play) {
        // var divPlay = $('<span class="rc-play"></span>');
        var svgPlay     = $(".rc-play", svg);
        var svgPlayR    = $("rect", svgPlay);
        currentX += hPad;
        svgPlay.attr("transform", 'translate(' + currentX + ',0) scale(0.8)');
        svgPlayR.attr({ width: 26, height: 26 });
        currentX += 26;
        hPad = 0;

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

        // svg /* divPlay */.append(svgPlay);
        svgPlayR /* divPlay */.click(function() {
            // $(self).trigger("play");
            if (model.playing()) model.pause(); else model.play();
        });
        // div.append(divPlay);

        $(model)
            .on("playing", updatePlay)
            .on("paused" , updatePlay)
            .on("ended"  , updatePlay);
    }

    ////////////////////////////////////////////////// elapsed

    if (optOpt.elapsed) {
        // var divElapsed = $('<span class="rc-timer"></span>');
        // div.append(divElapsed);
        var svgElapsed = $(".rc-elapsed", svg);
        hPad = Math.max(hPad, 4);
        currentX += hPad;
        svgElapsed.attr({x: currentX, y: 19});
        currentX += 36;
        hPad = 4;

        var updateElapsed = function() {
            var time = model.currentTime();
            self._updateTimer(svgElapsed, time);
        };

        updateElapsed();
        $(model).on("timeupdate", updateElapsed);
    }

    ////////////////////////////////////////////////// cue

    if (optOpt.cue) {
        var wc  = 32; // 64;
        var hc  = 14;
        var hch = hc/2;
        var rr  = 7;

        if (actualWidth > minWidth) wc += actualWidth - minWidth;   // use the rest

        var svgCue  = $('.rc-cue', svg);
        currentX += hPad;
        svgCue.attr("transform", 'translate(' + currentX + ',6)');

        var svgCueR = $("rect", svgCue);
        var svgCueC = $("circle", svgCue);
        svgCueR.attr({ rx: rr, ry: rr, width: wc, height: hc });
        svgCueC.attr({ cx: hch, cy: hch, r: (hch - 1), fill: bg });
        // divCue.append(svgCue);
        // div.append(divCue);
        currentX += wc;
        hPad = 2;

        var cueDragging     = false;
        var cueDragStartX   = 0;    // relative to window top-left
        var cueDragX        = 0;    // relative to window top-left
        var cueDragStartXM  = 0;    // relative to left margin of track
        var cueDragXM       = 0;    // relative to left margin of track
        var cueTime         = 0;    // model time as last reflected in the view

        var updateCue = function() {
            if (!cueDragging) {
                cueTime = model.currentTime();
                var dur = model.duration();
                if (isFinite(dur)) {
                    var cx = Math.max(0, Math.min(1, (cueTime / dur))) * (wc - hc) + hch;
                    $("circle", svgCue).attr("cx", cx);
                }
            }
        };

        updateCue();

        var dndPane = $('<div class="rc-drag"></div>');

        var cueDrag = function(e) {
            // console.log("cueDrag " + cueDragging);
            if (cueDragging) {
                cueDragX = e.clientX;
                var dx = cueDragX - cueDragStartX;
                var x0 = dx + cueDragStartXM;
                var x1 = Math.max(hch, Math.min(wc - hch, x0));
                // console.log("drag: dx = " + dx + "; cueDragCX = " + cueDragCX + "; hch = " + hch + "; wc-hch = " + (wc - hch) +  "; x0 = " + x0);
                cueDragXM = x1;
                svgCueC.attr("cx", x1);
                e.preventDefault();
            }
        };

        var cueDragUp = function(e) {
            // console.log("cueDragUp " + cueDragging);
            if (cueDragging) {
                cueDragging = false;
                e.preventDefault();
                dndPane.remove();

                var dur     = model.duration();
                var time    = ((cueDragXM - hch) / (wc - hc)) * dur;
                if (time != cueTime) {
                    // console.log("dragged " + (cueDragX - cueDragStartX));
                    model.currentTime(time);
                }
            }
        };

        var cueDragCancel = function() {
            if (cueDragging) {
                cueDragging = false;
                dndPane.remove();
                updateCue();
            }
        };

        // from circle
        var cueDragDownC = function (e) {
            cueDragStartX   = e.clientX;
            cueDragX        = cueDragStartX;
            cueDragging     = true;
            cueDragStartXM  = parseInt(svgCueC.attr("cx"));
            // console.log("down " + cueDragX + "; " + cueDragCX);
            e.preventDefault();
            dndPane.appendTo("body"); // $(document).append(dndPane);
            dndPane
                .mouseup(cueDragUp)
                .mouseleave(cueDragCancel)
                .mousemove(cueDrag);
        };

        // from track rect
        var cueDragDownR = function (e) {
            cueDragDownC(e);
            cueDragStartXM = e.pageX - svgCueR.offset().left; // cf. https://css-tricks.com/snippets/jquery/get-x-y-mouse-coordinates/
            //var x1 = Math.max(hch, Math.min(wc - hch, cueDragStartXM));
            //svgCueC.attr("cx", x1);
            cueDrag(e);
        };

        svgCueC.mousedown(cueDragDownC);
        // tricky stuff to keep dragging while leaving the circle
        svgCueR.mousedown(cueDragDownR);

        $(model).on("timeupdate", updateCue).on("durationchange", updateCue);
    }

    ////////////////////////////////////////////////// remaining

    if (optOpt.remaining) {
        //var divRemaining = $('<span class="rc-timer"></span>');
        //div.append(divRemaining);
        var svgRemaining = $(".rc-remaining", svg);
        hPad = Math.max(hPad, 4);
        currentX += hPad;
        svgRemaining.attr({x: currentX, y: 19});
        currentX += 36;
        hPad = 4;

        var updateRemaining = function() {
            var time = model.currentTime();
            var dur  = model.duration();
            self._updateTimer(svgRemaining, dur - time, true);
        };

        updateRemaining();
        $(model).on("timeupdate", updateRemaining).on("durationchange", updateRemaining);
    }

    ////////////////////////////////////////////////// volume

    if (optOpt.volume) {
        // var divVolume = $('<span class="rc-volume"></span>');
        var svgVolume   = $('.rc-volume', svg);
        var svgVolumeR  = $("rect", svgVolume);
        currentX += hPad;
        svgVolume.attr("transform", 'translate(' + currentX + ',0) scale(0.8)');
        svgVolumeR.attr({width: 26, height: 26 });
        currentX += 26;
        hPad = 0;

        var regularVolume   = model.volume();
        var muted           = false;

        var updateVolume = function() {
            var volume = model.volume();
            // console.log("volume = " + volume);
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

        // divVolume.append(svgVolume);
        svgVolumeR /* divVolume */.click(function() {
            if (muted) model.volume(regularVolume > 0 ? regularVolume : 1.0);
            else {
                regularVolume = model.volume();
                model.volume(0.0);
            }
            muted = !muted;
            // console.log("muted = " + muted);
        });
        // div.append(divVolume);

        $(model).on("volumechange", updateVolume);
    }

    $(elem).append(div1);

    ////////////////////////////////////////////////// meter

    if (optOpt.meter) {
        var wm      = elem.width();
        var hm      = 12;   // XXX TODO
        var canvas  = $('<canvas width="' + wm + '" height="' + hm + '" class="rc-meter"></canvas>');
        div.append(canvas);
        var canvasE = canvas[0]; // .get();

        var lastPeak    = 0.0;
        var lastRMS     = 0.0;
        var sqrSum      = 0.0;
        var sqrMax      = 0.0;
        var count       = 0;
        var lastPeakPx  = -1;   // other than zero so we initially paint once
        var lastRMSPx   = -1;

        var peak = function() {
            if (count > 0) lastPeak = Math.sqrt(sqrMax);
            return lastPeak;
        };

        var rms = function() {
            if (count > 0) lastRMS = Math.sqrt(sqrSum / count);
            return lastRMS;
        };

        var reset = function() {
            if (count > 0) {
                sqrSum  = 0.0;
                sqrMax  = 0.0;
                count   = 0;
            }
        };

        var meterAnalyze        = undefined;
        var meterDummy          = undefined;
        var meterTimerHandle    = undefined;    // interval-token

        var meterAnimStep = function() {
            var peakDB    = rc.ampdb(peak());
            var floorDB   = -48;
            var peakNorm  = peakDB / -floorDB + 1;
            var rmsDB     = rc.ampdb(rms());
            var rmsNorm   = rmsDB / -floorDB + 1;

            var px0   = Math.max(0, Math.min(wm, peakNorm * wm));
            var rx0   = Math.max(0, Math.min(wm, rmsNorm  * wm));
            var px    = Math.max(lastPeakPx - 4, px0);
            var rx    = Math.max(lastRMSPx  - 4, rx0);

            if (lastPeakPx != px || lastRMSPx != rx) {
                lastPeakPx  = px;
                lastRMSPx   = rx;
                // console.log("px = " + px + "; rx = " + rx + "; h = " + h);
                // console.log("peakDB = " + peakDB + "; rmsDB = " + rmsDB);
                var ctx     = canvasE.getContext("2d");
                ctx.fillStyle = bg; // "#000000";
                ctx.fillRect(0, 0, wm , hm);
                ctx.fillStyle = "#FFFFFF";
                ctx.fillRect(0, 0, px, hm);
                ctx.fillStyle = "#7F7F7F";
                ctx.fillRect(0, 0, rx, hm)
            } else {
                if (!meterAnalyze) {
                    lastPeak    = 0.0;
                    lastRMS     = 0.0;
                    if (px == 0 && rx == 0) stopMeterAnim();
                }
            }

            reset();
        };

        // var _foo = 0;

        var attachMeter = function() {
            var blockSize   = 512;
            var context     = rc.AudioContext();
            meterAnalyze    = context.createScriptProcessor(blockSize, 1, 1);
            meterAnalyze.onaudioprocess = function(e) {
                var inBuf   = e.inputBuffer;
                var numCh   = inBuf.numberOfChannels;
                for (var ch = 0; ch < numCh; ch++) {
                    var input = inBuf.getChannelData(ch);
                    var len   = input.length;
                    for (var idx = 0; idx < len; idx++) {
                        var x0 = input[idx];
                        var x = x0 * x0;
                        sqrSum += x;
                        if (x > sqrMax) sqrMax = x;
                    }
                    count += len;
                }
                // _foo++;
                // if (_foo % 100 == 0) {
                //    console.log("METER " + sqrMax);
                // }
            };
            // THIS IS NEEDED FOR CHROME
            // XXX TODO --- look into Tone.js, they
            // call some GC-prevention function that might serve the same purpose.
            meterDummy = context.createGain();
            meterDummy.gain.value = 0.0;
            meterAnalyze.connect(meterDummy);
            meterDummy.connect(context.destination);
            model.mediaNode().connect(meterAnalyze);
        };

        var detachMeter = function() {
            if (meterDummy) {
                if (meterAnalyze) {
                    meterAnalyze.disconnect(meterDummy);
                    meterAnalyze = undefined;
                }
                var context = meterDummy.context;
                meterDummy.disconnect(context.destination);
                meterDummy = undefined;
            }
        };

        var startMeterAnim = function() {
            stopMeterAnim();
            meterTimerHandle = window.setInterval(meterAnimStep, 33.3);
        };

        var stopMeterAnim = function() {
            if (meterTimerHandle) {
                // console.log("STOP METER");
                window.clearInterval(meterTimerHandle);
                meterTimerHandle = undefined;
            }
        };

        meterAnimStep();    // paint once

        $(model)
            .on("connected", function() {
                rc.log("connect meter");
                attachMeter();
                startMeterAnim();
            })
            .on("disconnected", function() {
                rc.log("disconnect meter");
                // let the meter fall to zero
                // stopMeterAnim(); -- the will now be detected in meterAnimStep
                detachMeter();
            });
    }

    ////////////////////////////////////////////////// hover

    if (optOpt.hover) {
        // XXX TODO -- doesn't seem to work
        $(model.element).tooltip({
            content: optOpt.hover
        });
    }
};