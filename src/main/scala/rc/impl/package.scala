package rc

package object impl {
  implicit class NodeImplOps(private val node: Node) extends AnyVal {
    def messageInlet(description: String)(fun: Message => Unit): Inlet =
      new MessageInletImpl(node, description, fun)

    def messageOutlet(description: String): Outlet with (Message => Unit) =
      new MessageOutletImpl(node, description)
  }
}
