/*
 *  Osc_~.scala
 *  (rc-player)
 *
 *  Copyright (c) 2015 Society of Artistic Research (SAR). All rights reserved.
 *  Written by Hanns Holger Rutz.
 *
 *	This software is published under a BSD 2-Clause License.
 *
 *
 *	For further information, please contact Hanns Holger Rutz at
 *	contact@sciss.de
 */

package rc
package objects

import org.scalajs.dom
import rc.audio.AudioSystem
import rc.impl.{AudioNodeImpl, ObjNodeImpl, OutletImpl, SingleInlet, SingleOutlet}

class Osc_~(val parent: Patcher, val args: List[Any])
  extends ObjNodeImpl("osc~")
  with AudioNodeImpl
  with SingleInlet with SingleOutlet { obj =>

  private var _freq: Double = args match {
    case (d: Double) :: Nil => d
    case Nil => 0
    case _ => throw new Exception(s"Illegal initial frequency parameter $args")
  }

  def freq: Double = _freq
  def freq_=(value: Double): Unit = if (_freq != value) {
    _freq = value
    oscillator.foreach { osc =>
      val time = AudioSystem.context.currentTime
      osc.frequency.cancelScheduledValues(time)
      osc.frequency.setValueAtTime(value, time)
      // osc.frequency.value = value
    }
  }

  private var oscillator = Option.empty[dom.OscillatorNode]

  protected def dspStarted(): Unit = if (oscillator.isEmpty) {
    val osc = AudioSystem.context.createOscillator()
    osc.frequency.value = _freq
    osc.start(AudioSystem.context.currentTime)
    oscillator = Some(osc)
  }

  protected def dspStopped(): Unit = {
    oscillator.foreach { osc =>
      osc.stop(AudioSystem.context.currentTime)
      oscillator = None
    }
  }

  object outlet extends OutletImpl {
    def node        = obj
    def tpe         = AudioType

    def audio: dom.AudioNode = oscillator.getOrElse {
      if (parent.dsp.active) dspStarted()
      oscillator.getOrElse(throw new Exception("DSP is not active"))
    }
  }

  val inlet = this.messageInlet {
    case M(d: Double) => freq = d
  }
}