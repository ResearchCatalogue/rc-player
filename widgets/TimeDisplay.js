$(function() {
    console.log("AQUI");
    var r = rc.Registry();
    r.addReceive("time", function(e) {
        console.log(e);
    });
});

