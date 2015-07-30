# AudioPlayer

The AudioPlayer plugin is a JQuery UI plugin that implements roughly the same functionality of the 
former audio player object in the RC, while using the new Web Audio API.

Invocation:

```javascript
$(parent).AudioPlayer(<options>)
```

An example is contained in the file `audioplayer.html`.

## Options
 
### Sound
 
The `sound`  structure has the following properties:

- `src` (required), the URL
- `start` (optional), start offset in the sound file in seconds
- `stop` (optional), stop offset in the sound file in seconds
- `gain` (optional), gain adjustment in decibels
- `fadein` (optional), pointing to a `fade` structure
- `fadeout` (optional), pointing to a `fade` structure
- `loop` (optional), a boolean (defaults to `false`) specifying whether to loop the sound region or not

A `fade` structure has the following properties:

- `duration` (required), length of fade in seconds
- `type`, being either `linear` (default) or `exponential`

### Style

The `style` object defines the positioning and appearance. It is described in [[Slidehow]].

### Options

The `options` section is used to customize the audio player.

- `autoplay`, a boolean; if `true`, file begins to play when page loads
- `stopothers`, a boolean; if `true`, pressing play stops all other audio players

A `controls` section:

- `play` - play/pause button
- `elapsed` - elapsed time display
- `cue` - show/cue position
- `remaining` - remaining time display
- `volume` - volume control
- `meter` - peak level meter
