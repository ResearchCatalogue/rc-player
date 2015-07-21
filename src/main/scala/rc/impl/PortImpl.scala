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

trait MessageInletImpl extends InletImpl {
  def accepts(tpe: Type): Boolean = tpe == MessageType
}

class MessageOutletImpl(val node: Node, val description: String) extends OutletImpl {
  def tpe: Type = MessageType

  def dispatch(message: Message): Unit = _cords.foreach { cord =>
    cord.sink ! message
  }
}