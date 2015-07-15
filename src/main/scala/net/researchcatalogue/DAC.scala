package net.researchcatalogue

import org.scalajs.dom.AudioNode

import scala.collection.mutable

class DAC(implicit system: AudioSystem) extends Obj {
  def id = "dac~"

  object in_~ extends AudioSink {
    private val map = mutable.Set.empty[AudioSource]

    def node: AudioNode = system.context.destination

    def add(source: AudioSource): Unit =
      if (map.add(source)) {
        source.add(this)
        source.node connect this.node
      }

    def remove(source: AudioSource): Unit =
      if (map.remove(source)) {
        source.remove(this)
        source.node disconnect this.node
      }
  }
}