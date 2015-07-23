package rc

package object impl {
  implicit class NodeImplOps(private val node: Node) extends AnyVal {
    def messageInlet(fun: M => Unit): Inlet =
      new MessageInletImpl(node, fun)

    def messageOutlet: Outlet with (M => Unit) =
      new MessageOutletImpl(node)
  }
}
