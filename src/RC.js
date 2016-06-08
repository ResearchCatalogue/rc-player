// console.log("build 4");

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

// missing functions on ancient jQuery
jQuery.fn.extend({

    on: function( types, selector, data, fn, /*INTERNAL*/ one ) {
        var origFn, type;

        // Types can be a map of types/handlers
        if ( typeof types === "object" ) {
            // ( types-Object, selector, data )
            if ( typeof selector !== "string" ) {
                // ( types-Object, data )
                data = data || selector;
                selector = undefined;
            }
            for ( type in types ) {
                this.on( type, selector, data, types[ type ], one );
            }
            return this;
        }

        if ( data == null && fn == null ) {
            // ( types, fn )
            fn = selector;
            data = selector = undefined;
        } else if ( fn == null ) {
            if ( typeof selector === "string" ) {
                // ( types, selector, fn )
                fn = data;
                data = undefined;
            } else {
                // ( types, data, fn )
                fn = data;
                data = selector;
                selector = undefined;
            }
        }
        if ( fn === false ) {
            fn = returnFalse;
        } else if ( !fn ) {
            return this;
        }

        if ( one === 1 ) {
            origFn = fn;
            fn = function( event ) {
                // Can use an empty set, since event contains the info
                jQuery().off( event );
                return origFn.apply( this, arguments );
            };
            // Use same guid so caller can remove using origFn
            fn.guid = origFn.guid || ( origFn.guid = jQuery.guid++ );
        }
        return this.each( function() {
            jQuery.event.add( this, types, fn, data, selector );
        });
    },
    one: function( types, selector, data, fn ) {
        return this.on( types, selector, data, fn, 1 );
    },
    off: function( types, selector, fn ) {
        var handleObj, type;
        if ( types && types.preventDefault && types.handleObj ) {
            // ( event )  dispatched jQuery.Event
            handleObj = types.handleObj;
            jQuery( types.delegateTarget ).off(
                handleObj.namespace ? handleObj.origType + "." + handleObj.namespace : handleObj.origType,
                handleObj.selector,
                handleObj.handler
            );
            return this;
        }
        if ( typeof types === "object" ) {
            // ( types-object [, selector] )
            for ( type in types ) {
                this.off( type, selector, types[ type ] );
            }
            return this;
        }
        if ( selector === false || typeof selector === "function" ) {
            // ( types [, fn] )
            fn = selector;
            selector = undefined;
        }
        if ( fn === false ) {
            fn = returnFalse;
        }
        return this.each(function() {
            jQuery.event.remove( this, types, fn, selector );
        });
    },

    trigger: function( type, data ) {
        return this.each(function() {
            jQuery.event.trigger( type, data, this );
        });
    },
    triggerHandler: function( type, data ) {
        var elem = this[0];
        if ( elem ) {
            return jQuery.event.trigger( type, data, elem, true );
        }
    }
});
