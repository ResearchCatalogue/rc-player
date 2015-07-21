package rc.sandbox

import org.scalajs.dom.AudioNode
import rc.audio.AudioSystem
import AudioSystem.context

class PhysicalOut extends HasIn with Obj {
  def id = "dac~"

  object in extends AudioSinkImpl {
    def node: AudioNode = context.destination
  }
}