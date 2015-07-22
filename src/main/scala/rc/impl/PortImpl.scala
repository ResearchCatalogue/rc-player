/*
 *  PortImpl.scala
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
package impl

trait PortImpl extends ModelImpl[Port.Update] {
  _: Port =>

  protected var _cords = List.empty[Cord]

  def cords: List[Cord] = _cords

  def removeCord(cord: Cord): Unit = {
    val i = _cords.indexOf(cord)
    if (i < 0) throw new Exception(s"Trying to remove a cord ($cord) that was not connected to port ($this)")
    _cords = _cords.patch(i, Nil, 1)
    dispatch(Port.CordRemoved(this, cord))
  }
}

trait InletImpl extends PortImpl with Inlet {
  def addCord(cord: Cord): Unit = {
    if (!accepts(cord.tpe)) throw new Exception(s"Cannot connect a cord of type ${cord.tpe} to this port ($this)")
    if (_cords.contains(cord)) throw new Exception(s"Cannot connect cord ($cord) twice to the same port ($this)")

    _cords ::= cord
    dispatch(Port.CordAdded(this, cord))
  }
}

trait OutletImpl extends PortImpl with Outlet {
  def addCord(cord: Cord): Unit = {
    if (this.tpe != cord.tpe) throw new Exception(s"Cannot connect a cord of type ${cord.tpe} to this port ($this)")
    if (_cords.contains(cord)) throw new Exception(s"Cannot connect cord ($cord) twice to the same port ($this)")

    _cords ::= cord
    dispatch(Port.CordAdded(this, cord))
  }
}

class MessageInletImpl(val node: Node, val description: String, fun: Message => Unit)
  extends InletImpl {

  def accepts(tpe: Type): Boolean = tpe == MessageType

  def ! (message: Message): Unit = fun(message)
}

class MessageOutletImpl(val node: Node, val description: String) extends OutletImpl with ((Message) => Unit) {
  def tpe: Type = MessageType

  def apply(message: Message): Unit = _cords.foreach { cord =>
    cord.sink ! message
  }
}

trait NoInlets {
  def inlets: List[Inlet] = Nil
}

trait NoOutlets {
  def outlets: List[Outlet] = Nil
}

trait SingleInlet {
  def inlets: List[Inlet] = inlet :: Nil
  def inlet: Inlet
}

trait SingleOutlet {
  def outlets: List[Outlet] = outlet :: Nil
  def outlet: Outlet
}
