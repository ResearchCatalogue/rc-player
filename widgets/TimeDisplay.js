$(function() {
    var r       = rc.Registry();
    var timer   = $("<div class='rc-timer' id='timer'>00:00</div>")
    $("body").append(timer)

    r.addReceive("time", function(e) {
        var secs    = e.atoms[0];
        var secsI   = Math.floor(secs)
        var mins    = Math.floor(secsI / 60)
        var secsM   = secsI % 60
        var text    = (mins > 9 ? "" : "0") + mins + ":" + (secsM > 9 ? "" : "0") + secsM;
        timer.text(text)
    });
});

