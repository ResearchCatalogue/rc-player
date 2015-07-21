package rc.sandbox

import scala.collection.mutable

trait AudioSinkImpl extends AudioSink {
  private val map = mutable.Set.empty[AudioSource]

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
