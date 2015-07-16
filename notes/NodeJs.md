The Ajax / XMLHttpRequest based media retrieval doesn't work with `file://` in the browser, that's why we
have to launch a real web server. Also note that remote URLs such as `http://sciss.de/noises2/staircase.mp3`
don't work because of CORS restrictions (Cross-origin resource sharing).

## Node.js

The currently chosen method for booting up a local web server is via Node.js with 
the [`http-server` plugin](https://stackoverflow.com/questions/16333790/node-js-quick-file-server-static-files-over-http).

    $ cd http-server/
    $ node bin/http-server
    
The contents of the `public` folder for testing:

```
lrwxrwxrwx 1 hhrutz hhrutz   38 Jul 16 02:34 rc-player -> /home/hhrutz/Documents/devel/rc-player
lrwxrwxrwx 1 hhrutz hhrutz   42 Jul  7 17:45 scala-js-test -> /home/hhrutz/Documents/devel/scala-js-test
lrwxrwxrwx 1 hhrutz hhrutz   67 Jul  7 17:46 staircase.mp3 -> /home/hhrutz/Documents/devel/minimal-mistakes/noises2/staircase.mp3
```
