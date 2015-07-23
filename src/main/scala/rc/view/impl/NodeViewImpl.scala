/*
 *  NodeViewImpl.scala
 *  (rc-player)
 *
 *  Copyright (c) 2015 Society of Artistic Research (SAR). All rights reserved.
 *  Written by Hanns Holger Rutz.
 *
 *	This software is published under the GNU General Public License v3+
 *
 *
 *	For further information, please contact Hanns Holger Rutz at
 *	contact@sciss.de
 */

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

  protected val ports: Map[Port, dom.svg.RectElement] = (elem.inlets ::: elem.outlets).map { port =>
    val py  = if (port.isInlet) 0 else boxHeight - 1
    val idx = port.index
    val px  = if (idx == 0) 0 else boxWidth - 7
    val res = rect(cls := "pat-port", x := px, y := py, width := 8, height := 2).render

//    if (this.isInstanceOf[MessageNodeViewImpl]) {
//      println(s"port $port, px = $px, py = $py, idx = $idx")
//    }

    (port, res)
  } (breakOut)

  def portLocation(port: Port): DoublePoint2D = {
    val r = ports(port)
    DoublePoint2D(r.x.baseVal.value + 4, r.y.baseVal.value + 1)
  }

  //  override protected def init(): Unit = {
  //    super.init()
  //    // XXX TODO --- create portlet
  //  }
}
