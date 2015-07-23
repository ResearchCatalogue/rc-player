/*
 *  Multiply_~.scala
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
import rc.audio.AudioSystem
import rc.impl.{InletImpl, OutletImpl, AudioNodeImpl, ObjNodeImpl, SingleOutlet}

class Multiply_~(val parent: Patcher, val args: List[Any])
  extends ObjNodeImpl("*~")
  with AudioNodeImpl
  with SingleOutlet { obj =>

  private var audioNodes = List.empty[dom.AudioNode]

  private var gain = Option.empty[dom.GainNode]

  private var _mul: Double = args match {
    case (d: Double) :: Nil => d
    case Nil => 0.0
    case _ => throw new Exception(s"Illegal initial multiplication parameter $args")
  }

  object outlet extends OutletImpl {
    def description = "Multiplied signal"
    def node        = obj
    def tpe         = AudioType

    def audio: dom.AudioNode = gain.getOrElse {
      if (parent.dsp.active) dspStarted()
      gain.getOrElse(throw new Exception("DSP is not active"))
    }
  }

  def mul: Double = _mul
  def mul_=(value: Double): Unit = if (_mul != value) {
    _mul = value
    gain.foreach { g =>
      val time = AudioSystem.context.currentTime
      g.gain.cancelScheduledValues(time)
      g.gain.setValueAtTime(value, time)
      // osc.frequency.value = value
    }
  }

  protected def dspStarted(): Unit = {
    val g = AudioSystem.context.createGain()
    g.gain.value = _mul
    audioNodes = inlet1.cords.map(_.source.audio)
    audioNodes.foreach(_.connect(g))
    gain = Some(g)
  }

  protected def dspStopped(): Unit = gain.foreach { g =>
    audioNodes.foreach(_.disconnect(g))
    audioNodes = Nil
    gain = None
  }

  object inlet1 extends InletImpl {
    def description = "Left operand (audio)"

    def accepts(tpe: Type) = tpe == AudioType

    def node: Node = obj

    def ! (message: Message): Unit = throw new Exception("Does not accept messages")

    override def cordAdded  (cord: Cord): Unit = gain.foreach { g =>
      val audioNode = cord.source.audio
      audioNodes ::= audioNode
      audioNode.connect(g)
    }

    override def cordRemoved(cord: Cord): Unit = gain.foreach { g =>
      val audioNode = cord.source.audio
      audioNodes = audioNodes.diff(audioNode :: Nil)
      audioNode.disconnect(g)
    }
  }

  val inlet2 = this.messageInlet("Right operand (audio or message)") {
    case Message(d: Double) => mul = d
  }

  def inlets: List[Inlet] = inlet1 :: inlet2 :: Nil
}