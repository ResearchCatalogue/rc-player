package rc

import org.scalajs.dom.AudioNode

import scala.collection.mutable

class DAC(implicit system: AudioSystem) extends HasIn with Obj {
  def id = "dac~"

  object in extends AudioSink {
    private val map = mutable.Set.empty[AudioSource]

    def node: AudioNode = system.context.destination

    def add(source: AudioSource): Unit =
      if (map.add(source)) {
        source.--->(this)
        source.node connect this.node
      }

    def remove(source: AudioSource): Unit =
      if (map.remove(source)) {
        source.-/->(this)
        source.node disconnect this.node
      }
  }
}