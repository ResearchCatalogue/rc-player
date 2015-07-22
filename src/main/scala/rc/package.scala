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
import rc.view.IntPoint2D

import scala.language.implicitConversions

package object rc {
  implicit def AudioContextExt(context: AudioContext): AudioContextExt =
    context.asInstanceOf[AudioContextExt]

  val isMac: Boolean = dom.navigator.platform.startsWith("Mac")

  /////////////////////////////

  implicit class PortOps(private val port: Port) extends AnyVal {
    def isInlet   : Boolean = port.isInstanceOf[Inlet ]
    def isOutlet  : Boolean = port.isInstanceOf[Outlet]

    /** Index in the list of inlets or outlets of the node. */
    def index     : Int     = {
      val n     = port.node
      val list  = if (isInlet) n.inlets else n.outlets
      list.indexOf(port)
    }
  }

  implicit class InletOps(private val inlet: Inlet) extends AnyVal {
    def acceptsMessages   : Boolean = inlet.accepts(MessageType)
    def acceptsAudio      : Boolean = inlet.accepts(AudioType  )
  }

  implicit class OutletOps(private val outlet: Outlet) extends AnyVal {
    def ---> (that: Inlet): Unit = {
      val cord = Cord(outlet, that)
      cord.parent.add(cord)
    }

    def -/-> (that: Inlet): Unit = {
      val cord = outlet.cords.find(_.sink == that)
        .getOrElse(throw new Exception(s"$outlet was not connected to $that"))

      cord.parent.remove(cord)
      cord.dispose()
    }
  }

  implicit class NodeOps(private val node: Node) extends AnyVal {
    def location: IntPoint2D = {
      node.state.get(State.Location) match {
        case Some(loc: IntPoint2D) => loc
        case _ => IntPoint2D(0, 0)
      }
    }

    def location_=(value: IntPoint2D): Unit =
      node.state.put(State.Location, value)
  }
}