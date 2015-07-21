package rc
package view

import org.scalajs.dom
import rc.sandbox.PatcherOLD

trait PatcherView extends View {
  def patcher: Patcher
  def container: dom.html.Element
}
