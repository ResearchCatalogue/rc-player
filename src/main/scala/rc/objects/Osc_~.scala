/*
 *  Osc_~.scala
 *  (rc-player)
 *
 *  Copyright (c) 2015 Society of Artistic Research (SAR). All rights reserved.
 *  Written by Hanns Holger Rutz.
 *
 *	This software is published under the GNU General Public License v3+
 *
 *
 *	For further information, please contact Hanns Holger Rutz at
 *	contact@sciss.de
 */

package rc
package objects

import org.scalajs.dom
import org.scalajs.dom.AudioNode
import rc.impl.{OutletImpl, AudioNodeImpl, ObjNodeImpl, SingleInlet, SingleOutlet}
import rc.audio.AudioSystem

class Osc_~(val parent: Patcher, val args: List[Any])
  extends ObjNodeImpl("osc~")
  with AudioNodeImpl
  with SingleInlet with SingleOutlet { obj =>

  private var _freq: Float = args match {
    case (i: Int  ) :: Nil => i
    case (f: Float) :: Nil => f
    case Nil => 0
    case _ => throw new Exception(s"Illegal initial frequency parameter $args")
  }

  def freq: Float = _freq
  def freq_=(value: Float): Unit = if (_freq != value) {
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
    def description = "Oscillator signal"
    def node        = obj
    def tpe         = AudioType

    def audio: AudioNode = oscillator.getOrElse {
      if (parent.dsp.active) dspStarted()
      oscillator.getOrElse(throw new Exception("DSP is not active"))
    }
  }

  val inlet = this.messageInlet("Frequency in Hertz") {
    case Message(i: Int  ) =>
      freq = i
    case Message(f: Float) =>
      freq = f
  }
}