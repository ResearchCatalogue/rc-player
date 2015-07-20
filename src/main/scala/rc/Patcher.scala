package rc

import org.scalajs.dom
import org.scalajs.dom.raw.KeyboardEvent
import org.scalajs.dom.{DragEvent, FocusEvent, Event, MouseEvent, Element}
import org.scalajs.jquery.{jQuery => $, JQuery}

import scala.scalajs.js
import scalatags.JsDom.all._

class Patcher extends Widget /* with js.Any */ { patcher =>
  def render: Element = {
    val tree = div(cls := "patcher", tabindex := 0)
    // {
    //  div(style := "position: relative; left: 40px; top: 40px; width: 40px; height: 40px; background: #FF0000")
    // }
    val elem = tree.render
    // $(elem).data(patcher)
    elem.onmousedown = { e: MouseEvent =>
      if (!e.defaultPrevented) {
        if (e.button == 0) {
          elem.focus()
        }
        if (e.button == 2) {
          // context-menu-button
        }
        e.preventDefault()
      }
    }

    var lastMouseX = 0.0
    var lastMouseY = 0.0

    elem.onmousemove = { e: MouseEvent =>
      lastMouseX = e.pageX - elem.offsetLeft
      lastMouseY = e.pageY - elem.offsetTop
      // println(s"x = $lastMouseX, y = $lastMouseY")
    }

    def putObject(): Unit = {
      val x         = lastMouseX.toInt
      val y         = lastMouseY.toInt
      val in        = input(cls := "edit-obj").render
      val childTree = div(cls := "obj incomplete", style := s"left:${x}px; top:${y}px")(in)
      val child     = childTree.render

      def updateWidth(len: Int): Unit = {
        val width = len * 7 + 4
        val ws    = s"${width}px"
        // println(s"New width = $width")
        in   .style.width = ws
        child.style.width = ws // this doesn't work: http://stackoverflow.com/questions/450903
      }

      val refreshWidth : js.Function0[Unit]         = () => updateWidth(in.value.length)
      val deferRefresh : js.Function1[Event, Unit] = { e: Event => dom.window.setTimeout(refreshWidth, 10); () }

      val checkWidth: js.Function1[Event, Unit] = { e: Event =>
        val len0  = in.value.length
        val len   = e match {
          case k: KeyboardEvent =>
            // println(s"KEY = '${k.key}'")
            deferRefresh(e)
            if (k.key == "Backspace" && k.key == "Del") len0 - 1 else len0 + 1
          case _ => len0
        }
        updateWidth(len)
      }

      def mkPort(jChild: JQuery, idx: Int, num: Int, isInlet: Boolean): Unit = {
        val x     = idx.toDouble / math.max(1, num - 1)
        val xp    = (x * 100).toInt
        val clz   = if (isInlet) "inlet" else "outlet"
        val port  = div(cls := s"$clz port", style := s"left: ${xp - 50}%").render
        jChild.append(port)
      }

      def mkObj(numInlets: Int, numOutlets: Int): Unit = {
        val jq = $(child)
        jq.removeClass("incomplete")
        jq.remove("#port")
        var i = 0
        while (i < numInlets) {
          mkPort(jq, i, numInlets, isInlet = true)
          i += 1
        }
        i = 0
        while (i < numOutlets) {
          mkPort(jq, i, numOutlets, isInlet = false)
          i += 1
        }
      }

      def validate(): Unit = {
        // XXX TODO
        in.value match {
          case "dac~" => mkObj(numInlets = 1, numOutlets = 0)
          case "osc~" => mkObj(numInlets = 2, numOutlets = 1)
          case _ => $(child).addClass("incomplete")
        }
      }

      in.onchange = { e: Event =>
        // user committed content
        checkWidth(e)
        validate()
      }
      in.onblur = { e: FocusEvent =>
        // user left input field
        checkWidth(e)
        validate()
      }
      in.oncut      = deferRefresh
      in.onpaste    = deferRefresh
      in.onkeypress = checkWidth  // cf. http://stackoverflow.com/questions/8795283

      elem.appendChild(child)
      in.focus()
    }

    elem.onkeydown = { e: KeyboardEvent =>
      if (!e.defaultPrevented) {
        val isMenu = if (isMac) e.metaKey else e.ctrlKey
        if (isMenu) {
          if (e.key == "1") {
            putObject()
            e.preventDefault()
          }
        }
        // println(s"key = ${e.key}; isMenu? $isMenu")
      }
    }

    elem
  }
}
