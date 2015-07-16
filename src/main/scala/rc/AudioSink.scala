package rc

import org.scalajs.dom.AudioNode

sealed trait AudioPort {
  def node: AudioNode
}

trait AudioSink extends AudioPort {
  def add   (source: AudioSource): Unit
  def remove(source: AudioSource): Unit
}

trait AudioSource extends AudioPort {
  def ---> (sink: AudioSink): AudioSink
  def -/-> (sink: AudioSink): AudioSink
}