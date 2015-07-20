package rc

import scala.collection.mutable

trait AudioSourceImpl extends AudioSource {
  private val map = mutable.Set.empty[AudioSink]

  def ---> (sink: AudioSink): AudioSink = {
    if (map.add(sink)) sink.add(this)
    sink
  }

  def -/-> (sink: AudioSink): AudioSink = {
    if (map.remove(sink)) sink.remove(this)
    sink
  }
}