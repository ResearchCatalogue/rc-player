package rc

import org.scalajs.dom

trait Widget {
  def render: dom.html.Element
}
