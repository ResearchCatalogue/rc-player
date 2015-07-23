package rc
package view

import rc.view.impl.ObjNodeViewImpl

object NodeView {
  /** Standard view for object nodes. */
  def apply(parentView: PatcherView, obj: ObjNode): NodeView =
    new ObjNodeViewImpl(parentView, obj, elemText = obj.contents)
}
trait NodeView extends View {
  override def elem: Node

  def portLocation(port: Port): DoublePoint2D
}
