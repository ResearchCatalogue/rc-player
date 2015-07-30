// console.log("build 3");

var rc = {
    logging: true,

    log: function (x) {
        if (rc.logging) console.log(x);
    },

    dbamp: function (x) {
        return Math.pow(10, x * 0.05);
    },

    _ampdbFactor: 20 / Math.log(10),

    ampdb: function(x) {
        return Math.log(x) * rc._ampdbFactor
    },

    AudioContext: function () {
        if (!rc._context) rc._context = new window.AudioContext;
        return rc._context;
    },

    style: function(elem, style) {
        var pos = style.position;
        if (pos) {
            elem.css("position", "absolute");
            elem.css("left"  , pos.left  );
            elem.css("top"   , pos.top   );
            elem.css("width" , pos.width );
            elem.css("height", pos.height);
        }
        var pad = style.padding;
        if (pad) {
            elem.css("padding-left"  , pad.left  );
            elem.css("padding-top"   , pad.top   );
            elem.css("padding-bottom", pad.bottom);
            elem.css("padding-right" , pad.right );
        }
        var bd = style.border;
        if (bd) {
            elem.css("border-style" , bd.style   );
            elem.css("border-width" , bd.strength);
            elem.css("border-color" , bd.color   );
            elem.css("border-radius", bd.radius  );
        }
        var bg = style.background;
        if (bg) {
            elem.css("background-color"   , bg.color   );
            elem.css("background-image"   , bg.image   );
            elem.css("background-repeat"  , bg.repeat  );
            elem.css("background-position", bg.position);
            // TODO: `size`: `auto`, `cover`, `contain`
        }
        // TODO: opacity
    }
};