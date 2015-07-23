/*
 *  Dac_~.scala
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
import rc.impl.{AudioNodeImpl, InletImpl, NoArgs, NoOutlets, ObjNodeImpl, SingleInlet}

class Dac_~(val parent: Patcher)
  extends ObjNodeImpl("dac~")
  with AudioNodeImpl
  with SingleInlet with NoOutlets with NoArgs { obj =>

  private var audioNodes = List.empty[dom.AudioNode]

  private def audioTarget = AudioSystem.context.destination

  protected def dspStarted(): Unit = {
    audioNodes = inlet.cords.collect {
      case cord if cord.tpe == AudioType => cord.source.audio
    }
    // println(s"dac~ connecting ${audioNodes.size} nodes.")
    audioNodes.foreach(_.connect(audioTarget))
  }

  protected def dspStopped(): Unit = {
    audioNodes.foreach(_.disconnect(audioTarget))
    audioNodes = Nil
  }

  object inlet extends InletImpl {
    def accepts(tpe: Type) = true

    def node: Node = obj

    /** Tries to send a message into this inlet. Throws an error if `MessageType` is not accepted. */
    def ! (message: Message): Unit = message match {
      case Message(0)      | Message("stop" ) => parent.dsp.active = false
      case Message(_: Int) | Message("start") => parent.dsp.active = true
    }

    override def cordAdded  (cord: Cord): Unit = if (cord.tpe == AudioType && parent.dsp.active) {
      val audioNode = cord.source.audio
      audioNodes ::= audioNode
      audioNode.connect(audioTarget)
    }

    override def cordRemoved(cord: Cord): Unit = if (cord.tpe == AudioType && parent.dsp.active) {
      val audioNode = cord.source.audio
      audioNodes = audioNodes.diff(audioNode :: Nil)
      audioNode.disconnect(audioTarget)
    }
  }
}
