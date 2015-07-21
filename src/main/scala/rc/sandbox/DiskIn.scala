package rc.sandbox

import org.scalajs.dom.AudioNode
import org.scalajs.dom.raw.HTMLMediaElement
import rc.audio.AudioSystem.context

class DiskIn(elem: HTMLMediaElement) extends Generator {
  def play(): Unit = {
    elem.play()
  }

  object out extends AudioSourceImpl {
    val node: AudioNode = context.createMediaElementSource(elem)
  }
}