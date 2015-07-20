package rc

import org.scalajs.dom.AudioNode
import rc.AudioSystem.context

class PhysicalOut extends HasIn with Obj {
  def id = "dac~"

  object in extends AudioSinkImpl {
    def node: AudioNode = context.destination
  }
}