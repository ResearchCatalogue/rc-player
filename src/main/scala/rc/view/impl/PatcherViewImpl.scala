/*
 *  PatcherViewImpl.scala
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
import rc.impl.ModelImpl

import scala.annotation.switch
import scala.collection.mutable
import scalatags.JsDom.all._
import scalatags.JsDom.svgTags._

class PatcherViewImpl(val patcher: Patcher)
  extends PatcherView with ModelImpl[PatcherView.Update] {

  val peer      : dom.svg .Element = svg(cls := "pat").render
  val container : dom.html.Element = div(cls := "pat", tabindex := 0)(peer).render

  val selection: PatcherSelection = PatcherSelection(this)

  private var lastMouseX = 0.0
  private var lastMouseY = 0.0
  private var _editing = false

  private val views     = mutable.Buffer.empty[View]
  private val viewMap   = mutable.Map.empty[Elem, View]

  init()

  def editing: Boolean = _editing
  def editing_=(value: Boolean): Unit = if (_editing != value) {
    _editing = value
    dispatch(PatcherView.ModeChanged(this, editing = value))
  }

  private def addViews(xs: Seq[View]): Unit = {
    views ++= xs
    xs.foreach { view =>
      viewMap.put(view.elem, view)
      peer.appendChild(view.peer)
    }
    dispatch(PatcherView.Added(this, xs: _*))
  }

  private def removeViews(xs: Seq[View]): Unit = {
    selection.remove(xs: _*)
    views --= xs
    xs.foreach { view =>
      viewMap.remove(view.elem)
      peer.removeChild(view.peer)
    }
    dispatch(PatcherView.Removed(this, xs: _*))
    xs.foreach(_.dispose())
  }

  private def init(): Unit = {
    container.onmousedown = { e: dom.MouseEvent =>
      if (!e.defaultPrevented) {
        if (e.button == 0) {
          selection.clear()
          container.focus()
          e.preventDefault()
        }
        // else if (e.button == 2) {
        //   // context-menu-button
        // }
      }
    }

    container.onmousemove = { e: dom.MouseEvent =>
      lastMouseX = e.pageX - container.offsetLeft
      lastMouseY = e.pageY - container.offsetTop
      // println(s"x = $lastMouseX, y = $lastMouseY")
    }

    container.onkeydown = { e: dom.KeyboardEvent =>
      if (!e.defaultPrevented) {
        val isMenu = if (isMac) e.metaKey else e.ctrlKey
        if (isMenu) {
          // println(s"key = ${e.keyCode}")
          (e.keyCode: @switch) match {
            case '1' =>
              println("BOX") // putBox()
              e.preventDefault()

            case 'B' =>
              putBang()
              e.preventDefault()

            case _ =>
          }
        } else {
          if (e.keyCode == 8 || e.keyCode == 46) { // backspace or delete
            if (selection.nonEmpty) {
              val toRemove = selection.get
              selection.clear()
              println("TODO: REMOVE")
              // toRemove.foreach(deleteBox)
            }
            e.preventDefault()
          }
        }
        // println(s"key = ${e.key}; isMenu? $isMenu")
      }
    }

    patcher.addListener {
      case Patcher.Added(_, elems @ _*) =>
        val views = elems.map(_.view())
        addViews(views)
      case Patcher.Removed(_, elems @ _*) =>
        removeViews(views)
    }
  }

  private def putBang(): Unit = {

  }
}
