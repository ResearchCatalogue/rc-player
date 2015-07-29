console.log("build 3");

var rc = {
    dbamp: function (x) {
        return Math.pow(10, x * 0.05);
    },

    logging: false,

    log: function (x) {
        if (rc.logging) console.log(x);
    },

    AudioContext: function () {
        if (!rc._context) rc._context = new window.AudioContext;
        return rc._context;
    }
}