package rc.audio

import org.scalajs.dom.raw.AudioContext

object AudioSystem {
  lazy val instance: AudioSystem = new AudioSystem

  def context: AudioContext = instance.context
}
class AudioSystem {
  val context = new AudioContext
}
