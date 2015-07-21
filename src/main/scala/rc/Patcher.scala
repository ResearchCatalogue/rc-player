/*
 *  Patcher.scala
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

import org.scalajs.dom
import org.scalajs.jquery.{jQuery => $}
import rc.impl.PatcherImpl

import scalatags.JsDom.all._

object Patcher {
  def apply(): Patcher = new PatcherImpl
}
trait Patcher extends Widget {
  private def mkConnection(a: dom.html.Element, b: dom.html.Element): Unit = {
    val aIn   = $(".inlet" , a)
    val aOut  = $(".outlet", a)
    val bIn   = $(".inlet" , b)
    val bOut  = $(".outlet", b)

    if (aOut.length > 0 && bIn.length > 0) {
      println("Patch a to b")
      patch(aOut(0), bIn(0))
    } else if (bOut.length > 0 && aIn.length > 0) {
      println("Patch b to a")
      patch(bOut(0), aIn(0))
    } else {
      println("Not enough inlets/outlets")
    }
  }

  private def patch(source: dom.html.Element, sink: dom.html.Element): Unit = {
    val svgElem = $(".cables")(0).asInstanceOf[dom.svg.SVG]

    val sourceP = source.offsetParent.asInstanceOf[dom.html.Element]
    val sinkP   = sink  .offsetParent.asInstanceOf[dom.html.Element]

    val sourceX   = source.offsetLeft + sourceP.offsetLeft + 4
    val sourceY   = source.offsetTop  + sourceP.offsetTop  + 1.5
    val sinkX     = sink  .offsetLeft + sinkP  .offsetLeft + 4
    val sinkY     = sink  .offsetTop  + sinkP  .offsetTop  + 1.5

    println(s"Patching ($sourceX, $sourceY) -> ($sinkX, $sinkY)")

    import scalatags.JsDom.svgTags._
    val lineElem = line(cls := "cord").render
    lineElem.x1.baseVal.value = sourceX
    lineElem.y1.baseVal.value = sourceY
    lineElem.x2.baseVal.value = sinkX
    lineElem.y2.baseVal.value = sinkY
    svgElem.appendChild(lineElem)
  }

//  private def patchOLD(source: dom.html.Element, sink: dom.html.Element): Unit = {
//    // div(style := "position: absolute;")
//    val c0 = $(".cables")
//    val c  = if (c0.length > 0) c0.apply(0).asInstanceOf[dom.html.Canvas] else {
//      val tree = canvas(cls := "cables").render
//      tree.width  = 400
//      tree.height = 400
//      $(".patcher").append(tree)
//      tree
//    }
//
//    val sourceP = source.offsetParent.asInstanceOf[dom.html.Element]
//    val sinkP   = sink  .offsetParent.asInstanceOf[dom.html.Element]
//
////    val parentPos = $(".patcher")(0)
////    val sourcePos = $(source    )(0)
////    val sinkPos   = $(sink      ).position().asInstanceOf[js.Dynamic]
////    val parentX   = parentPos.left.asInstanceOf[Int]
////    val parentY   = parentPos.top .asInstanceOf[Int]
//    val sourceX   = source.offsetLeft + sourceP.offsetLeft + 4
//    val sourceY   = source.offsetTop  + sourceP.offsetTop  + 1.5
//    val sinkX     = sink  .offsetLeft + sinkP  .offsetLeft + 4
//    val sinkY     = sink  .offsetTop  + sinkP  .offsetTop  + 1.5
//
//    println(s"Patching ($sourceX, $sourceY) -> ($sinkX, $sinkY)")
//
//    val ctx = c.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
//    ctx.lineWidth = 1.5
//    ctx.strokeStyle = "rgb(80,  80, 128)"
//    ctx.moveTo(sourceX, sourceY)
//    ctx.lineTo(sinkX  , sinkY  )
//    ctx.stroke()
//  }
}