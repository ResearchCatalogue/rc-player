package rc
package sandbox

import org.scalajs.dom.AudioNode
import rc.audio.AudioProcessingEvent
import rc.audio.AudioSystem.context

import scala.scalajs.js

class WhiteNoise extends Generator {
  object out extends AudioSourceImpl {
    val node: AudioNode = {
      val res = context.createScriptProcessor(4096, 0, 1)
      res.onaudioprocess = { e: AudioProcessingEvent =>
        // e.playbackTime
        val output = e.outputBuffer.getChannelData(0)
        var i = 0; while (i < 4096) {
          output(i) = (js.Math.random() * 2 - 1).toFloat
        i += 1
        }
      }
      res
    }
  }
}
