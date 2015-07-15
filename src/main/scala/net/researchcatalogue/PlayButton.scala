package net.researchcatalogue

import org.scalajs.dom.Element

import scalatags.JsDom.all._

class PlayButton extends Widget {
  def render: Element = {
    button(cls := "play").render
  }
}
