/*
 *  BoxImpl.scala
 *  (rc-player)
 *
 *  Copyright (c) 2015 Society of Artistic Research (SAR). All rights reserved.
 *  Written by Hanns Holger Rutz.
 *
 *	This software is published under a BSD 2-Clause License.
 *
 *
 *	For further information, please contact Hanns Holger Rutz at
 *	contact@sciss.de
 */

package rc.sandbox

import org.scalajs.dom
import rc.view.{IntPoint2D, IntSize2D}

import scala.scalajs.js
import scalatags.JsDom.all._

class BoxImpl(val patcher: PatcherOLD) extends Box {
  private var _loc      = IntPoint2D(0, 0)
  private val _inlets   = js.Array[PortOLD]()
  private val _outlets  = js.Array[PortOLD]()

  private val inputElem = input(cls := "edit-obj").render
  private val divElem   = div(cls := "obj incomplete")(inputElem).render

  init()

  def location: IntPoint2D = _loc
  def size    : IntSize2D  = IntSize2D(divElem.clientWidth, divElem.clientHeight)

  def numInlets   = _inlets.length
  def numOutlets  = _outlets.length

  def render: dom.html.Element = divElem

  def location_=(value: IntPoint2D): Unit = if (_loc != value) {
    _loc = value
    val st = render.style
    st.left = s"${_loc.x}px"
    st.top  = s"${_loc.y}px"
  }

  def focus(): Unit = inputElem.focus()

  def dispose(): Unit = {
    disposePorts(_inlets )
    disposePorts(_outlets)
  }

  private def disposePorts(coll: js.Array[PortOLD]): Unit = {
    coll.foreach { port =>
      divElem.removeChild(port.render)
      port.dispose()
    }
    coll.length = 0
  }

  private def updateWidth(len: Int): Unit = {
    val width = len * 7 + 2 // 4
    // println(s"New width = $width")
    // $(in).outerWidth()
    inputElem.style.width = s"${width}px"
    divElem  .style.width = s"${width + 6}px" // this doesn't work: http://stackoverflow.com/questions/450903
  }

  private def mkPort(index: Int, num: Int, isInlet: Boolean): PortOLD = {
    val port      = PortOLD(this, isInlet = isInlet, index = index)
    val portElem  = port.render
    val x         = index.toDouble / math.max(1, num - 1)
    val xp        = (x * 100).toInt
    if (xp == 0) {
      portElem.style.left = "0%"
    } else {
      portElem.style.right = "0%"
    } // else ...?

    divElem.appendChild(portElem)
    port
  }

  private def mkPorts(num: Int, isInlet: Boolean): Unit = {
    val coll = if (isInlet) _inlets else _outlets
    disposePorts(coll)

    coll.length = num
    var i = 0
    while (i < num) {
      val port = mkPort(index = i, num = num, isInlet = isInlet)
      coll(i) = port
      divElem.appendChild(port.render)
      i += 1
    }
  }

  private def mkObj(numInlets: Int, numOutlets: Int): Unit = {
    inputElem.classList.remove("incomplete")
    mkPorts(num = numInlets , isInlet = true )
    mkPorts(num = numOutlets, isInlet = false)
  }

  private def init(): Unit = {
    val refreshWidth : js.Function0[Unit]             = () => updateWidth(inputElem.value.length)
    val deferRefresh : js.Function1[dom.Event, Unit]  = { e: dom.Event => dom.window.setTimeout(refreshWidth, 10); () }

    val checkWidth: js.Function1[dom.Event, Unit] = { e: dom.Event =>
      val len0  = inputElem.value.length
      val len   = e match {
        case k: dom.KeyboardEvent =>
          // println(s"KEY = '${k.keyCode}'")
          deferRefresh(e)
          // if (k.key == "Backspace" && k.key == "Del") len0 - 1 else len0 + 1
          len0 + 1 // if (k.keyCode == 8 || k.keyCode == 46) len0 - 1 else len0 + 1
        case _ => len0
      }
      updateWidth(len)
    }

    def validate(): Unit = {
      // XXX TODO
      inputElem.value match {
        case "dac~" => mkObj(numInlets = 1, numOutlets = 0)
        case "osc~" => mkObj(numInlets = 2, numOutlets = 1)
        case _ => inputElem.classList.add("incomplete")
      }
    }

    inputElem.onchange = { e: dom.Event =>
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

    divElem.onmousedown = { e: dom.MouseEvent =>
      if (e.button == 0 && !e.defaultPrevented) {
        val sel = patcher.selection
        if (!sel.contains(this)) {
          if (!e.shiftKey) sel.clear()
          sel.add(this)
        }
        new DragBox(this, e)
        // e.stopPropagation()
        e.preventDefault()
      }
    }
  }
}


private[sandbox] class DragBox(box: Box, e0: dom.MouseEvent) {
  private val elem  = box.patcher.render
  private val loc0  = box.location
  private val dim   = box.size
  private val pDim  = box.patcher.size

  // println("New DragConnection")

  private var started = false

  private val mouseUp: js.Function1[dom.MouseEvent, Unit] = { e: dom.MouseEvent =>
    detach()
    if (started) {
      box.render.style.zIndex = "0"
    }
  }

  private val mouseMove: js.Function1[dom.MouseEvent, Unit] = { e: dom.MouseEvent =>
    val dx = (e.clientX - e0.clientX).toInt
    val dy = (e.clientY - e0.clientY).toInt
    // println(s"dx = $dx, dy = $dy")

    if (!started) {
      started = dx * dx + dy * dy > 4
      if (started) {
        // println("DRAG BEGIN")
        box.render.style.zIndex = "1"
      }
    }
    if (started) {
      val boxX = js.Math.max(0, js.Math.min(loc0.x + dx, pDim.width  - dim.width ))
      val boxY = js.Math.max(0, js.Math.min(loc0.y + dy, pDim.height - dim.height))
      box.location = IntPoint2D(boxX, boxY)
    }
  }

  elem.addEventListener("mouseup"  , mouseUp  )
  elem.addEventListener("mousemove", mouseMove)

  private def detach(): Unit = {
    elem.removeEventListener("mouseup"  , mouseUp  )
    elem.removeEventListener("mousemove", mouseMove)
  }
}