package rc
package view

import org.scalajs.dom

trait PatcherView extends View {
  def patcher: Patcher
  def container: dom.html.Element
}
