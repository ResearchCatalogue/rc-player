# AudioRegion

The `AudioRegion` class encapsulates the functionality of playing a sound file or piece of a sound file.
To instantiate:

    rc.AudioRegion(options)
    
The `options` object has the following properties:

- `src` (required), the URL
- `start` (optional), start offset in the sound file in seconds
- `stop` (optional), stop offset in the sound file in seconds
- `gain` (optional), gain adjustment in decibels
- `fadein` (optional), pointing to a `fade` structure
- `fadeout` (optional), pointing to a `fade` structure
- `loop` (optional), a boolean (defaults to `false`) specifying whether to loop the sound region or not

The region creates an HTML5 `<audio>` node responsible for the playback, and feeds it into the Web Audio API 
in order to allow fading or further processing.

The following methods are supported:

- `playing()`: returns a boolean indicating the current status
- `currentTime()`: returns number of seconds of current position, offset against region start
- `currentTime(x)`: sets current position, as offset in seconds against region start
- `duration()`: returns the duration of the region in seconds
- `volume()`: returns the current volume as a linear factor (1.0 = 0 dB)
- `volume(x)`: sets the current volume given as a linear factor (1.0 = 0 dB)
- `play()`: Puts the region into playing mode. If the media element is ready to play,
            playing commences immediately, otherwise
            it will begin as soon as enough buffered
            data is available. If the region is currently playing, calling
            `play` again will result in an abrupt `stop()`
            before beginning playback again.
- `pause()`: stops the playback, maintaining the current time position
- `stop()`: stops the playback, resetting the current time position
- `release(x)`: fades out the sound and then stops it. The argument specifies the fade time in seconds.
- `dispose()`: Destroys this audio region, stopping any
               currently playing node, clearing the scheduler
               and releasing resources as far as possible.
               It is not possible to use this audio region
               beyond this call.
- `releaseAndDispose(x)`: Like `release()` but with a successive `dispose()`.
                          This can be used to "fade-out and forget" this region.
                          The argument specifies the fade time in seconds.
- `mediaNode()`: returns the media-element-source `AudioNode` (Web Audio API) on which this region is based.
- `audioElem()`: returns the HTML5 `<audio>` element. 
