/*
 *  PatcherViewImpl.scala
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

package rc
package view
package impl

import org.scalajs.dom
import rc.impl.ModelImpl
import rc.objects.Button

import scala.annotation.switch
import scala.collection.{breakOut, mutable}
import scalatags.JsDom.all._
import scalatags.JsDom.svgTags._

class PatcherViewImpl(val patcher: Patcher)
  extends PatcherView with ModelImpl[PatcherView.Update] {

  val peer      : dom.svg .Element = svg(cls := "pat").render
  val container : dom.html.Element = div(cls := "pat", tabindex := 0)(peer).render

  val selection: PatcherSelection = PatcherSelection(this)

  private var lastMouseX = 0
  private var lastMouseY = 0
  private var _editing = false

  private val viewSeq   = mutable.Buffer.empty[View]
  private val viewMap   = mutable.Map.empty[Elem, View]

  init()

  private def lastMouseLoc = IntPoint2D(lastMouseX, lastMouseY)

  def editing: Boolean = _editing
  def editing_=(value: Boolean): Unit = if (_editing != value) {
    _editing = value
    dispatch(PatcherView.ModeChanged(this, editing = value))
  }

  def getView(elem: Elem): Option[View] = viewMap.get(elem)

  private def addViews(elems: Seq[Elem]): Unit = {
    val views = elems.map(_.view(this))
    viewSeq ++= views
    views.foreach { view =>
      viewMap.put(view.elem, view)
      peer.appendChild(view.peer)
    }
    dispatch(PatcherView.Added(this, views: _*))
  }

  private def removeViews(elems: Seq[Elem]): Unit = {
    val views = elems.flatMap(viewMap.remove)
    selection.remove(views: _*)
    viewSeq --= views
    views.foreach { view =>
      peer.removeChild(view.peer)
    }
    dispatch(PatcherView.Removed(this, views: _*))
    views.foreach(_.dispose())
  }

  private def init(): Unit = {
    container.mousePressed { e =>
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

    container.mouseMoved { e =>
      lastMouseX = (e.pageX - container.offsetLeft).toInt
      lastMouseY = (e.pageY - container.offsetTop ).toInt
      // println(s"x = $lastMouseX, y = $lastMouseY")
    }

    container.keyPressed { e =>
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
              patcher.remove(toRemove.map(_.elem)(breakOut): _*)
            }
            e.preventDefault()
          }
        }
        // println(s"key = ${e.key}; isMenu? $isMenu")
      }
    }

    patcher.addListener {
      case Patcher.Added(_, elems @ _*) =>
        addViews(elems)
      case Patcher.Removed(_, elems @ _*) =>
        removeViews(elems)
    }

    addViews(patcher.elems)
  }

  private def putBang(): Unit = {
    val bang      = new Button(patcher)
    bang.location = lastMouseLoc
    patcher.add(bang)
    createdNodeFromGUI(bang)
  }

  private def createdNodeFromGUI(n: Node): Unit =
    viewMap.get(n).foreach { view =>
      selection.clear()
      selection.add(view)
    }
}
