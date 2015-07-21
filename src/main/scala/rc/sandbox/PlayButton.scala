package rc.sandbox

import org.scalajs.dom

import scalatags.JsDom.all._

class PlayButton extends Widget {
  def render: dom.html.Element = {
    button(cls := "play").render
  }
}
