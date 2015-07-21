package rc.sandbox

import org.scalajs.dom._
import rc.audio.AudioSystem.context

class Gain(foo: Double) extends Filter { gain =>
  private val node: AudioNode = {
    val res = context.createGain()
    res.gain.value = foo
    res
  }

  object in extends AudioSinkImpl {
    def node = gain.node
  }

  object out extends AudioSourceImpl {
    def node = gain.node
  }
}