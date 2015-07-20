package rc

import org.scalajs.dom.raw.KeyboardEvent
import org.scalajs.dom.{MouseEvent, Element}
import org.scalajs.jquery.{jQuery => $}

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
      lastMouseX = e.pageX // - elem.offsetLeft
      lastMouseY = e.pageY // - elem.offsetTop
    }

    elem.onkeydown = { e: KeyboardEvent =>
      if (!e.defaultPrevented) {
        val isMenu = if (isMac) e.metaKey else e.ctrlKey
        if (isMenu) {
          if (e.key == "1") {
            val x = lastMouseX.toInt
            val y = lastMouseY.toInt
            val childTree = div(cls := "obj incomplete", style := s"left: ${x}px; top:${y}px") {
              input(cls := "edit-obj")
            }
            val child = childTree.render
            elem.appendChild(child)
            // child.focus()
            e.preventDefault()
          }
        }
        // println(s"key = ${e.key}; isMenu? $isMenu")
      }
    }

    elem
  }
}
