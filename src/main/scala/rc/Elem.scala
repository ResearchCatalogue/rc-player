/*
 *  Elem.scala
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

import rc.view.View

sealed trait Elem {
  def parent: Patcher
  def dispose(): Unit
  def view(): View
}

sealed trait Node extends Elem {
  def inlets : List[Inlet ]
  def outlets: List[Outlet]
}

trait MessageNode extends Node {
  var content: Message
}

trait ObjNode extends Node {
  def name: String
}

trait Cord extends Elem {
  def source: Outlet
  def sink  : Inlet
  def tpe   : Type
}

object Port {
  sealed trait Update { def port: Port }
  case class CordAdded  (port: Port, cord: Cord) extends Update
  case class CordRemoved(port: Port, cord: Cord) extends Update
}
sealed trait Port extends Model[Port.Update] {
  def description : String

  def node: Node

  def cords: List[Cord]

  def addCord   (cord: Cord): Unit
  def removeCord(cord: Cord): Unit
}

trait Inlet extends Port {
  def accepts(tpe: Type): Boolean

  def ! (message: Message): Unit
}

trait Outlet extends Port {
  def tpe: Type
}

object Message {
  val Bang = Message("bang")
}
case class Message(atoms: Any*)

sealed trait Type
case object AudioType   extends Type
case object MessageType extends Type