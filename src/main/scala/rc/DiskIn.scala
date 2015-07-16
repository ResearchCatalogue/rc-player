package rc

import org.scalajs.dom.AudioNode
import org.scalajs.dom.raw.HTMLMediaElement
import org.scalajs.jquery.{jQuery => $}

import scala.collection.mutable

class DiskIn(elem: HTMLMediaElement)(implicit system: AudioSystem) extends Generator {
  object out extends AudioSource {
    private val map = mutable.Set.empty[AudioSink]

    val node: AudioNode = system.context.createMediaElementSource(elem)

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
