package rc
package view

import org.scalajs.dom

object View {
  /** Standard view for object nodes. */
  def apply(n: ObjNode): View = ???
}
trait View {
  def elem: dom.svg.Element
}
