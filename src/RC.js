// console.log("build 3");

var rc = {
    dbamp: function (x) {
        return Math.pow(10, x * 0.05);
    },

    logging: true,

    log: function (x) {
        if (rc.logging) console.log(x);
    },

    AudioContext: function () {
        if (!rc._context) rc._context = new window.AudioContext;
        return rc._context;
    },

    style: function(elem, style) {
        if (style.position) {
            var pos = style.position;
            elem.css("left"  , pos.left  );
            elem.css("top"   , pos.top   );
            elem.css("width" , pos.width );
            elem.css("height", pos.height);
        }
        // TODO: padding, border, etc. goes here
    }
};