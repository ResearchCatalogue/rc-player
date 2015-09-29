# rc-player

The rc-player project aims at the development of new interactive and composable media players for the 
[Research Catalogue](http://www.researchcatalogue.net/) platform.
This project is (C)opyright 2015 by the Society  of Artistic Research (SAR). All rights reserved.
Written by Hanns Holger Rutz. This software is published under a BSD 2-Clause License.

The project is currently in incubation.

## test page

The current test pages are `audioplayer.html` and `slideshow.html`.
The easiest way to run them locally is via Node.js.

- `npm install http-server`
- `ln -s <location-to-rc-player-directory> <location-to-http-server-directory>/public/`

The second line makes a symbolic link into the server's publicly served root folder.
Then to start the server:

- `cd <location-to-http-server-directory>`
- `node bin/http-server`

The pages can be opened at 
[http://0.0.0.0:8080/rc-player/audioplayer.html](http://0.0.0.0:8080/rc-player/audioplayer.html)
and
[http://0.0.0.0:8080/rc-player/slideshow.html](http://0.0.0.0:8080/rc-player/slideshow.html)

Note that you cannot enter the address `0.0.0.0` into Chrome directly due to a bug in Chrome.
Instead you can create a bookmark for this address.

## credits

Uses some of Raphael.js' [icons](http://raphaeljs.com/icons) which are released under the MIT License.