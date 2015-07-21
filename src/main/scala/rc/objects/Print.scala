package rc
package objects

import rc.impl.{InletImpl, ModelImpl}
import rc.view.View

class Print(prefix: Boolean) extends ObjNode { obj =>

  def this() = this(prefix = true)

  def this(args: List[String]) = this {
    var prefix = true
    args.foreach {
      case "-n" => prefix = false
      case other => throw new Exception(s"Print - illegal option $other")
    }
    prefix
  }

  def name = "Print"

  def outlets: List[Outlet] = Nil

  def inlets: List[Inlet] = inlet :: Nil

  def dispose(): Unit = ()

  def view(): View = View(this)

  private object inlet extends InletImpl with ModelImpl[Port.Update] {
    def description: String = "Messages to Print"

    def node: Node = obj

    def accepts(tpe: Type): Boolean = tpe == MessageType

    def ! (message: Message): Unit = {
      val m = message.atoms.mkString(" ")
      val s = if (prefix) s"print: $m" else m
      println(s)
    }
  }
}
