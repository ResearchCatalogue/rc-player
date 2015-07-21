/*
 *  BoxImpl.scala
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
import org.scalajs.dom._
import org.scalajs.jquery.{jQuery => $}

import scala.scalajs.js
import scalatags.JsDom.all._

class BoxImpl extends Box {
  private var _loc = IntPoint2D(0, 0)

  def loc: IntPoint2D = _loc

  private val inputElem = input(cls := "edit-obj").render
  private val divElem   = div(cls := "obj incomplete", style := s"left:${_loc.x}px; top:${_loc.y}px")(inputElem).render

  def render: dom.html.Element = divElem

  def loc_=(value: IntPoint2D): Unit = if (_loc != value) {
    _loc = value
    val st = render.style
    st.left = s"${_loc.x}px"
    st.top  = s"${_loc.y}px"
  }

  def focus(): Unit = inputElem.focus()

  init()

  private def updateWidth(len: Int): Unit = {
    val width = len * 7 + 2 // 4
    // println(s"New width = $width")
    // $(in).outerWidth()
    inputElem.style.width = s"${width}px"
    divElem  .style.width = s"${width + 6}px" // this doesn't work: http://stackoverflow.com/questions/450903
  }

  private def init(): Unit = {
    val refreshWidth : js.Function0[Unit]        = () => updateWidth(inputElem.value.length)
    val deferRefresh : js.Function1[Event, Unit] = { e: Event => dom.window.setTimeout(refreshWidth, 10); () }

    val checkWidth: js.Function1[Event, Unit] = { e: Event =>
      val len0  = inputElem.value.length
      val len   = e match {
        case k: KeyboardEvent =>
          // println(s"KEY = '${k.keyCode}'")
          deferRefresh(e)
          // if (k.key == "Backspace" && k.key == "Del") len0 - 1 else len0 + 1
          len0 + 1 // if (k.keyCode == 8 || k.keyCode == 46) len0 - 1 else len0 + 1
        case _ => len0
      }
      updateWidth(len)
    }

    def mkPort(child: dom.html.Element, idx: Int, num: Int, isInlet: Boolean): Unit = {
      // println("mkPort")
      val x     = idx.toDouble / math.max(1, num - 1)
      val xp    = (x * 100).toInt
      val clz   = if (isInlet) "inlet" else "outlet"
      val xps   = if (xp == 0) "left: 0%" else /* if (xp == 100) */ "right: 0%" // else ...?
      val port  = div(cls := s"$clz port", style := xps).render
      child.appendChild(port)
    }

    def mkObj(numInlets: Int, numOutlets: Int): Unit = {
      val jq = $(divElem)
      $(inputElem).removeClass("incomplete")
      jq.remove(".port")
      var i = 0
      while (i < numInlets) {
        mkPort(divElem, i, numInlets, isInlet = true)
        i += 1
      }
      i = 0
      while (i < numOutlets) {
        mkPort(divElem, i, numOutlets, isInlet = false)
        i += 1
      }
    }

    def validate(): Unit = {
      // XXX TODO
      inputElem.value match {
        case "dac~" => mkObj(numInlets = 1, numOutlets = 0)
        case "osc~" => mkObj(numInlets = 2, numOutlets = 1)
        case _ => $(inputElem).addClass("incomplete")
      }
    }

    inputElem.onchange = { e: Event =>
      // println("onchange")
      // user committed content
      checkWidth(e)
      validate()
    }

    //      in.onblur = { e: FocusEvent =>
    //        println("onblur")
    //        // XXX TODO -- testing one two testing
    //        val objects = $(".obj")
    //        println(s"Now there are ${objects.length} objects.")
    //        if (objects.length == 2) {
    //          mkConnection(objects(0), objects(1))
    //        }
    //      }

    inputElem.oncut      = deferRefresh
    inputElem.onpaste    = deferRefresh
    inputElem.onkeydown  = checkWidth  // cf. http://stackoverflow.com/questions/8795283

    updateWidth(0)
    // elem.appendChild(child)
    // in.focus()
  }
}
