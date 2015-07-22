package rc
package view
package impl

trait NodeViewImpl extends ViewImpl with NodeView {
  def portLocation(port: Port): IntPoint2D =
    if (port.isInlet) IntPoint2D(4, 1) else IntPoint2D(4, 20)

  override protected def init(): Unit = {
    super.init()
    // XXX TODO --- create portlet
  }
}
