package rc

import org.scalajs.dom._

import scala.collection.mutable
import scala.scalajs.js

class Meter(implicit system: AudioSystem) extends HasIn { meter =>
  private var lastPeak  = 0.0
  private var lastRMS   = 0.0
  private var sqrSum    = 0.0f
  private var sqrMax    = 0.0f
  private var count     = 0

  def peak: Double  = {
    if (count > 0) lastPeak = js.Math.sqrt(sqrMax)
    lastPeak
  }

  def rms: Double = {
    if (count > 0) lastRMS = js.Math.sqrt(sqrSum / count)
    lastRMS
  }

  def reset(): Unit = if (count > 0) {
    // println(s"resetting from $sqrSum and $sqrMax")
    sqrSum  = 0f
    sqrMax  = 0f
    count   = 0
  }

  private val node: AudioNode = {
    import system.context
    val inNode  = context.createGain()
    val squared = context.createGain()

    val analyze = system.context.createScriptProcessor(0, 1, 0)
    analyze.onaudioprocess = { e: AudioProcessingEvent =>
      val input = e.inputBuffer.getChannelData(0)
      val len   = input.length
      var i = 0; while (i < len) {
        val x = input(i)
        sqrSum += x
        if (x > sqrMax) sqrMax = x
        i += 1
      }
      count += len
    }

    inNode  connect squared
    inNode  connect squared.gain
    squared connect analyze

    inNode
  }

  object in extends AudioSink {
    private val map = mutable.Set.empty[AudioSource]

    def node: AudioNode = meter.node

    def add(source: AudioSource): Unit =
      if (map.add(source)) {
        source.--->(this)
        source.node connect this.node
      }

    def remove(source: AudioSource): Unit =
      if (map.remove(source)) {
        source.-/->(this)
        source.node disconnect this.node
      }
  }
}
