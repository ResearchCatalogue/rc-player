package rc
package sandbox

import org.scalajs.dom
import rc.view.{IntPoint2D, IntSize2D}

import scala.scalajs.js
import scalatags.JsDom.all._
import scalatags.JsDom.svgTags.svg

class PatcherOLDImpl extends PatcherOLD {
  val cableElement    = svg(cls := "cables").render
  private val divElem = div(cls := "patcher", tabindex := 0)(cableElement).render
  private var lastMouseX = 0.0
  private var lastMouseY = 0.0

  val selection: PatcherSelection = new PatcherSelectionImpl(this)

  init()

  def render: dom.html.Element = divElem

  def size: IntSize2D  = IntSize2D(divElem.clientWidth, divElem.clientHeight)

  private val boxes = js.Array[Box]()

  private def putBox(): Box = {
    val x         = lastMouseX.toInt
    val y         = lastMouseY.toInt
    val box       = Box(this)
    boxes.push(box)
    box.location  = IntPoint2D(x, y)
    divElem.appendChild(box.render)
    box.focus()
    box
  }

  private def deleteBox(box: Box): Unit = {
    divElem.removeChild(box.render)
    box.dispose()
  }

  private def init(): Unit = {
    divElem.onmousedown = { e: dom.MouseEvent =>
      if (!e.defaultPrevented) {
        if (e.button == 0) {
          selection.clear()
          divElem.focus()
          e.preventDefault()
        }
        // else if (e.button == 2) {
        //   // context-menu-button
        // }
      }
    }

    divElem.onmousemove = { e: dom.MouseEvent =>
      lastMouseX = e.pageX - divElem.offsetLeft
      lastMouseY = e.pageY - divElem.offsetTop
      // println(s"x = $lastMouseX, y = $lastMouseY")
    }

    divElem.onkeydown = { e: dom.KeyboardEvent =>
      if (!e.defaultPrevented) {
        val isMenu = if (isMac) e.metaKey else e.ctrlKey
        if (isMenu) {
          // println(s"key = ${e.keyCode}")
          if (e.keyCode == '1') {
            putBox()
            e.preventDefault()
          }
        } else {
          if (e.keyCode == 8 || e.keyCode == 46) { // backspace or delete
            if (selection.nonEmpty) {
              val toRemove = selection.get
              selection.clear()
              toRemove.foreach(deleteBox)
            }
            e.preventDefault()
          }
        }
        // println(s"key = ${e.key}; isMenu? $isMenu")
      }
    }

    //    selection.addListener {
    //      case PatcherSelection.Update(added, removed) =>
    //        if (removed.nonEmpty) println(removed.mkString("REMOVED: ", ", ", ""))
    //        if (added  .nonEmpty) println(added  .mkString("ADDED: ", ", ", ""))
    //    }
  }
}
