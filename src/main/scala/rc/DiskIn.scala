package rc

import org.scalajs.dom.AudioNode
import org.scalajs.dom.raw.HTMLMediaElement
import org.scalajs.jquery.{jQuery => $}

import AudioSystem.context

class DiskIn(elem: HTMLMediaElement) extends Generator {
  def play(): Unit = {
    elem.play()
  }

  object out extends AudioSourceImpl {
    val node: AudioNode = context.createMediaElementSource(elem)
  }
}