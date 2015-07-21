package rc
package impl

import org.scalajs.dom.{svg => _, _}
import org.scalajs.jquery.{jQuery => $}

import scalatags.JsDom.all._
import scalatags.JsDom.svgTags.svg

class PatcherImpl extends Patcher {
  private val svgElem = svg(cls := "cables").render
  private val divElem = div(cls := "patcher", tabindex := 0)(svgElem).render
  private var lastMouseX = 0.0
  private var lastMouseY = 0.0

  def render: Element = divElem

  init()

  private def putObject(): Unit = {
    val x         = lastMouseX.toInt
    val y         = lastMouseY.toInt
    val box       = Box()
    box.loc       = IntPoint2D(x, y)
    divElem.appendChild(box.render)
    box.focus()
  }

  private def init(): Unit = {
    divElem.onmousedown = { e: MouseEvent =>
      if (!e.defaultPrevented) {
        if (e.button == 0) {
          divElem.focus()
        }
        if (e.button == 2) {
          // context-menu-button
        }
        e.preventDefault()
      }
    }

    divElem.onmousemove = { e: MouseEvent =>
      lastMouseX = e.pageX - divElem.offsetLeft
      lastMouseY = e.pageY - divElem.offsetTop
      // println(s"x = $lastMouseX, y = $lastMouseY")
    }

    divElem.onkeydown = { e: KeyboardEvent =>
      if (!e.defaultPrevented) {
        val isMenu = if (isMac) e.metaKey else e.ctrlKey
        if (isMenu) {
          // println(s"key = ${e.keyCode}")
          if (e.keyCode == '1') {
            putObject()
            e.preventDefault()
          }
        }
        // println(s"key = ${e.key}; isMenu? $isMenu")
      }
    }
  }
}
