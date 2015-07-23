package rc
package view
package impl

import org.scalajs.dom

import scalatags.JsDom.all.{width => _, height => _, _}
import scalatags.JsDom.svgAttrs._
import scalatags.JsDom.svgTags._

import scala.collection.breakOut

trait NodeViewImpl extends ViewImpl with NodeView {
  protected def boxWidth : Int
  protected def boxHeight: Int = 20

  private val ports: Map[Port, dom.svg.RectElement] = (elem.inlets ::: elem.outlets).map { port =>
    val py  = if (port.isInlet) 0 else boxHeight - 1
    val idx = port.index
    val px  = if (idx == 0) 0 else boxWidth - 7
    val res = rect(cls := "pat-port", x := px, y := py, width := 8, height := 2).render
    (port, res)
  } (breakOut)

  val peer: dom.svg.G = {
    val loc       = elem.location
    val rectTree  = rect(cls := "pat-node", x := 0.5, y := 0.5, width := boxWidth, height := boxHeight)
    val res       = g(cls := "pat-node", rectTree, transform := s"translate(${loc.x},${loc.y})").render
    ports.foreach { case (_, port) =>
      res.appendChild(port)
    }
    res
  }

  def portLocation(port: Port): DoublePoint2D = {
    val r = ports(port)
    DoublePoint2D(r.x.baseVal.value + 4, r.y.baseVal.value + 1)
  }

  //  override protected def init(): Unit = {
  //    super.init()
  //    // XXX TODO --- create portlet
  //  }
}
