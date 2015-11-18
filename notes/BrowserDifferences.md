## AudioContext.createMediaElementSource

- Chrome preserves the `<audio>` elements controls
- Firefox still shows the controls, but they are dysfunctional (e.g., pressing the play button has no effect)
- It is still possible in both cases to programmatically invoke `.play()`
- Safari 8 does not play actual audio signal through the Web Audio API in this case. This is fixed in Safari 9

## Feedback

- Mozilla doesn't seem to handle feedback networks correctly. Listen for example to the last bit of this: 
http://blog.chrislowis.co.uk/2014/07/23/dub-delay-web-audio-api.html - sounds fine in Chromium and distorted in Firefox

## Usage

[Usage Statistics](http://www.w3schools.com/browsers/browsers_stats.asp)
