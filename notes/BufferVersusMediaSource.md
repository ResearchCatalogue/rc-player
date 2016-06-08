- only buffer node can be scheduled with audio accuracy
- can we use a buffer node to dynamically stream a media element?
  i.e. can we control the speed of the ajax request or partition it?
- difficult to stream into buffers; e.g. 
  [this question](https://stackoverflow.com/questions/20134384/web-audio-api-how-to-play-a-stream-of-mp3-chunks)
  one would need probably some dedicated sockets instead of `XmlHttpRequest`
- Safari's support for media-element-source is abysmal. Each `play()`
  may result in a hiccup with the previous buffer content.