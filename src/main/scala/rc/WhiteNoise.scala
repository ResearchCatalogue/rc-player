package rc

import org.scalajs.dom.AudioNode
import rc.audio.{AudioProcessingEvent, AudioSystem}
import AudioSystem.context
import rc.sandbox.{AudioSourceImpl, Generator}

import scala.scalajs.js

class WhiteNoise extends Generator {
  object out extends AudioSourceImpl {
    val node: AudioNode = {
      val res = context.createScriptProcessor(4096, 0, 1)
      res.onaudioprocess = { e: AudioProcessingEvent =>
        // e.playbackTime
        val output = e.outputBuffer.getChannelData(0)
        var i = 0; while (i < 4096) {
          output(i) = js.Math.random().toFloat * 2 - 1
          i += 1
        }
      }
      res
    }
  }
}
