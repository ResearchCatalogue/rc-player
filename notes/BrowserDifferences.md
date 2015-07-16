## AudioContext.createMediaElementSource

- Chrome preserves the `<audio>` elements controls
- Firefox still shows the controls, but they are dysfunctional (e.g., pressing the play button has no effect)
- It is still possible in both cases to programmatically invoke `.play()`
