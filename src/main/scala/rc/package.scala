/*
 *  package.scala
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

import org.scalajs.dom
import org.scalajs.dom.AudioContext
import rc.audio.AudioContextExt
import rc.sandbox.{AudioSource, AudioSink, HasOut, HasIn}
import rc.view.IntPoint2D

import scala.language.implicitConversions

package object rc {
  implicit def AudioContextExt(context: AudioContext): AudioContextExt =
    context.asInstanceOf[AudioContextExt]

  // implicit def defaultOut(h: HasOut): AudioSource = h.out

  implicit final class HasOutOps(private val h: HasOut) extends AnyVal {
    def ---> (sink: AudioSink): Unit = h.out ---> sink
    def ---> [B <: HasIn](target: B): target.type = { h.out ---> target.in; target }
  }

  implicit final class SourceOps(private val source: AudioSource) extends AnyVal {
    def ---> [B <: HasIn](target: B): target.type = { source ---> target.in; target }
  }

  val isMac: Boolean = dom.navigator.platform.startsWith("Mac")

  /////////////////////////////

  implicit class PortOps(private val p: Port) extends AnyVal {
    def isInlet   : Boolean = p.isInstanceOf[Inlet ]
    def isOutlet  : Boolean = p.isInstanceOf[Outlet]

    /** Index in the list of inlets or outlets of the node. */
    def index     : Int     = {
      val n     = p.node
      val list  = if (isInlet) n.inlets else n.outlets
      list.indexOf(p)
    }
  }

  implicit class InletOps(private val i: Inlet) extends AnyVal {
    def acceptsMessages   : Boolean = i.accepts(MessageType)
    def acceptsAudio      : Boolean = i.accepts(AudioType  )
  }

  implicit class NodeOps(private val n: Node) extends AnyVal {
    def location: IntPoint2D = {
      n.state.get(State.Location) match {
        case Some(loc: IntPoint2D) => loc
        case _ => IntPoint2D(0, 0)
      }
    }

    def location_=(value: IntPoint2D): Unit =
      n.state.put(State.Location, value)
  }
}