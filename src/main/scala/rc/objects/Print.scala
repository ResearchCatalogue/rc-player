package rc
package objects

import rc.impl.{InletImpl, ModelImpl}
import rc.view.View

class Print(val parent: Patcher, prefix: String) extends ObjNode { obj =>
  /** Default prefix is `"print"` */
  def this(parent: Patcher) = this(parent, prefix = "print: ")

  def this(parent: Patcher, args: List[String]) = this(parent, {
    args match {
      case "-n" :: Nil => ""
      case _ => args.mkString("", " ", ": ")
    }
  })

  def name = "print"

  def inlets : List[Inlet ] = inlet :: Nil
  def outlets: List[Outlet] = Nil

  def dispose(): Unit = ()

  def view(): View = View(this)

  private object inlet extends InletImpl with ModelImpl[Port.Update] {
    def description: String = "Messages to Print"

    def node: Node = obj

    def accepts(tpe: Type): Boolean = tpe == MessageType

    def ! (message: Message): Unit = {
      val m = message.atoms.mkString(prefix, " ", "")
      println(m)
    }
  }
}
