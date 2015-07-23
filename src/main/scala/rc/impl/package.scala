package rc

package object impl {
  implicit class NodeImplOps(private val node: Node) extends AnyVal {
    def messageInlet(fun: Message => Unit): Inlet =
      new MessageInletImpl(node, fun)

    def messageOutlet: Outlet with (Message => Unit) =
      new MessageOutletImpl(node)
  }
}
