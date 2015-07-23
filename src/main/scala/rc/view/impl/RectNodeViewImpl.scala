package rc.view.impl

import org.scalajs.dom

import scalatags.JsDom.all.{width => _, height => _, _}
import scalatags.JsDom.svgAttrs._
import scalatags.JsDom.svgTags._

trait RectNodeViewImpl extends NodeViewImpl {
  val peer: dom.svg.G = {
    val loc       = elem.location
    val rectTree  = rect(cls := "pat-node", x := 0.5, y := 0.5, width := boxWidth, height := boxHeight)
    val res       = g(cls := "pat-node", rectTree, transform := s"translate(${loc.x},${loc.y})").render
    ports.foreach { case (_, port) =>
      res.appendChild(port)
    }
    res
  }
}
