/*
 *  PortImpl.scala
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
package impl

import org.scalajs.dom

import scala.scalajs.js
import scalatags.JsDom.all._
import scalatags.JsDom.svgTags.line

class PortImpl(val box: Box, val isInlet: Boolean, val index: Int) extends Port {
  private val elem = div(cls := s"port ${if (isInlet) "inlet" else "outlet"}").render

  init()

  def render: dom.html.Element = elem

  def location: IntPoint2D  = {
    // IntPoint2D(elem.clientLeft , elem.clientTop   )
    IntPoint2D(elem.offsetLeft.toInt, elem.offsetTop.toInt)
  }

  def size    : IntSize2D   = IntSize2D (elem.clientWidth, elem.clientHeight)

  private def init(): Unit = {
    elem.onmousedown = { e: dom.MouseEvent =>
      if (e.button == 0 && !e.defaultPrevented) {
        new DragConnection(this, e)
        // e.stopPropagation()
        e.preventDefault()
      }
    }
  }
}

private[impl] class DragConnection(port: Port, e0: dom.MouseEvent) {
  // import port.{render => elem}
  private val box   = port.box
  private val elem  = box.patcher.render
  private val cable = box.patcher.cableElement
  private val loc0  = {
    val pLoc  = port.location
    val pSz   = port.size
    val c     = IntPoint2D(pLoc.x + pSz.width / 2, pLoc.y + pSz.height / 2)
    // println(s"center = $c")
    port.box.location + c
  }

  // println("New DragConnection")

  private var started = false
  private var lineElem: dom.svg.Line = _

  private val mouseUp: js.Function1[dom.MouseEvent, Unit] = { e: dom.MouseEvent =>
    detach()
    if (started) {
      cable.removeChild(lineElem)
      lineElem = null
    }
  }

  private val mouseMove: js.Function1[dom.MouseEvent, Unit] = { e: dom.MouseEvent =>
    val dx = (e.clientX - e0.clientX).toInt
    val dy = (e.clientY - e0.clientY).toInt
    // println(s"dx = $dx, dy = $dy")

    val wasStarted = started

    if (!wasStarted) {
      started = dx * dx + dy * dy > 4
      if (started) lineElem = line(cls := "cord" /* , style := "z-index: 1" */).render
    }
    if (started) {
      lineElem.x1.baseVal.value = loc0.x
      lineElem.y1.baseVal.value = loc0.y
      lineElem.x2.baseVal.value = loc0.x + dx
      lineElem.y2.baseVal.value = loc0.y + dy
    }

    if (wasStarted) cable.appendChild(lineElem)

    ()
  }

  elem.addEventListener("mouseup"  , mouseUp  )
  elem.addEventListener("mousemove", mouseMove)

  private def detach(): Unit = {
    elem.removeEventListener("mouseup"  , mouseUp  )
    elem.removeEventListener("mousemove", mouseMove)
  }
}