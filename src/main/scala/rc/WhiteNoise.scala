package rc

import org.scalajs.dom.AudioNode

import scala.collection.mutable
import scala.scalajs.js

class WhiteNoise(implicit system: AudioSystem) extends Generator {
  object out extends AudioSource {
    private val map = mutable.Set.empty[AudioSink]

    val node: AudioNode = {
      val res = system.context.createScriptProcessor(4096, 1, 1)
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

    def --->(sink: AudioSink): AudioSink = {
      if (map.add   (sink)) sink.add   (this)
      sink
    }

    def -/->(sink: AudioSink): AudioSink = {
      if (map.remove(sink)) sink.remove(this)
      sink
    }
  }
}
