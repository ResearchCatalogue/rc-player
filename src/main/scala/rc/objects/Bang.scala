package rc
package objects

import rc.impl.{MessageInletImpl, MessageOutletImpl}
import rc.view.{BangView, View}

class Bang(val parent: Patcher) extends ObjNode { obj =>
  def name = "bang"

  def inlets : List[Inlet ] = inlet  :: Nil
  def outlets: List[Outlet] = outlet :: Nil

  def dispose(): Unit = ()

  def view(): View = BangView(this)

  private object inlet extends MessageInletImpl {
    def description: String = "Any Message Triggers a Bang"

    def node: Node = obj

    def ! (message: Message): Unit = outlet.dispatch(Message.Bang)
  }
  private val outlet = new MessageOutletImpl(this, "Bang Messages")
}
