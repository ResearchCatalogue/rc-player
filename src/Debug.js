// create audio context
var AudioContext = window.AudioContext || window.webkitAudioContext;
var audioCtx = new AudioContext();

// set basic variables for example
var myAudio = document.querySelector('audio');
var pre = document.querySelector('pre');
var myScript = document.querySelector('script');

pre.innerHTML = myScript.innerHTML;

var linearRampPlus = document.querySelector('.linear-ramp-plus');
var linearRampMinus = document.querySelector('.linear-ramp-minus');

// Create a MediaElementAudioSourceNode
// Feed the HTMLMediaElement into it
var source = audioCtx.createMediaElementSource(myAudio);

// Create a gain node and set it's gain value to 0.5
var gainNode = audioCtx.createGain();
gainNode.gain.value = 0.5;
var currGain = gainNode.gain.value;

// connect the AudioBufferSourceNode to the gainNode
// and the gainNode to the destination
source.connect(gainNode);
gainNode.connect(audioCtx.destination);

// set buttons to do something onclick
linearRampPlus.onclick = function() {
    currGain = 1.0;
    gainNode.gain.linearRampToValueAtTime(1.0, audioCtx.currentTime + 2);
}

linearRampMinus.onclick = function() {
    currGain = 0;
    gainNode.gain.linearRampToValueAtTime(0, audioCtx.currentTime + 2);
}