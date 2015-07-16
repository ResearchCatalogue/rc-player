package rc

import org.scalajs.dom._

import scala.collection.mutable

class Gain(foo: Double)(implicit system: AudioSystem) extends Filter { gain =>
  private val node: AudioNode = {
    val res = system.context.createGain()
    res.gain.value = foo
    res
  }

  object in extends AudioSink {
    private val map = mutable.Set.empty[AudioSource]

    def node = gain.node

    def add(source: AudioSource): Unit =
      if (map.add(source)) {
        source ---> this
        source.node connect this.node
      }

    def remove(source: AudioSource): Unit =
      if (map.remove(source)) {
        source -/-> this
        source.node disconnect this.node
      }
  }

  object out extends AudioSource {
    private val map = mutable.Set.empty[AudioSink]

    def node = gain.node

    def ---> (sink: AudioSink): AudioSink = {
      if (map.add   (sink)) sink.add   (this)
      sink
    }

    def -/-> (sink: AudioSink): AudioSink = {
      if (map.remove(sink)) sink.remove(this)
      sink
    }
  }
}
