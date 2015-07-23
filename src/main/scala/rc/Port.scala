/*
 *  Port.scala
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

import org.scalajs.dom

object Port {
  sealed trait Update { def port: Port }
  case class CordAdded  (port: Port, cord: Cord) extends Update
  case class CordRemoved(port: Port, cord: Cord) extends Update
}
sealed trait Port extends Model[Port.Update] {
  def node: Node

  def cords: List[Cord]

  def addCord   (cord: Cord): Unit
  def removeCord(cord: Cord): Unit
}

trait Inlet extends Port {
  def accepts(tpe: Type): Boolean

  /** Tries to send a message into this inlet. Throws an error if `MessageType` is not accepted. */
  def ! (message: Message): Unit
}

trait Outlet extends Port {
  def tpe: Type

  /** Tries to retrieve the audio node whose output can be connected. Throws an error if `tpe` is not `AudioType`. */
  def audio: dom.AudioNode
}
