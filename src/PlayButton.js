$.widget("rc.PlayButton", {
    _create: function () {
        this._button = $('<button>');
        this._button.addClass("rc-play");
        this._button.append('<svg width="32" height="32"><path d="M6.684,25.682L24.316,15.5L6.684,5.318V25.682z"></path></svg>');
        $(this.element).append(this._button);
    }
});