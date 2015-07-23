package rc
package impl

import rc.view.{View, PatcherView, MessageNodeView}

import scala.scalajs.js

class MessageNodeImpl(val parent: Patcher, var args: List[Any])
  extends NodeImpl
  with MessageNode
  with ModelImpl[String] {

  private val _vars = js.Array[Any]()
  private var _message: Message = null

  private def argsUpdated(): Unit = {
    _message  = null // invalidate
    var idx = 0; while (idx < 9) { _vars(idx) = 0; idx += 1 }
    dispatch(contents)
  }

  private def updateMessage(): Unit = {
    val b = List.newBuilder[Any]
    args.foreach {
      case arg: String if arg.length == 2 && arg.charAt(0) == '$' =>
        val c = arg.charAt(1)
        if (c >= '0' && c <= '9') _vars(c - '0') else arg

      case arg => b += arg
    }
    _message = Message(b.result(): _*)
  }

  def message: Message = {
    if (_message == null) updateMessage()
    _message
  }

  def contents: String = args.mkString(" ")

  val outlet = this.messageOutlet

  val inlet = this.messageInlet {
    case Message.Bang =>
      outlet(message)
    case Message("set", rest @ _*) =>
      args = rest.toList
      argsUpdated()
    case Message("append", rest @ _*) =>
      args ++= rest
      argsUpdated()
    case Message("prepend", rest @ _*) =>
      args :::= rest.toList
      argsUpdated()
    case other =>
      val it  = other.atoms.iterator
      var idx = 0
      while (it.hasNext && idx < 9) {
        _vars(idx) = it.next()
        idx += 1
      }
      outlet(message)
  }

  def view(parentView: PatcherView): View = MessageNodeView(parentView, this)
}
