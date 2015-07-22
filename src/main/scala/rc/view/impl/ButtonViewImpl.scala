/*
 *  BangViewImpl.scala
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
import rc.objects.Button

import scala.scalajs.js

class ButtonViewImpl(val parentView: PatcherView, val elem: Button) extends NodeViewImpl {
  import scalatags.JsDom.all.{width => _, height => _, _}
  import scalatags.JsDom.svgTags._
  import scalatags.JsDom.svgAttrs._


  val peer: dom.svg.Element = {
    val loc         = elem.location
    // XXX TODO -- would be great if we could keep these size values in a CSS somehow
    val rectTree    = rect  (cls := "pat-node pat-button", x := 0.5, y := 0.5, width := 20, height := 20)
    val circleTree  = circle(cls := "pat-button", cx := 10.5, cy := 10.5, r := 7.5 /* 8.5 */)
    val groupElem   = g(cls := "pat-node", rectTree, circleTree, transform := s"translate(${loc.x},${loc.y})").render
    groupElem
  }

  private val elemL = elem.addListener { case _ => flash() }

  init()

  private var flashHandle = Int.MinValue

  private val stopFlash: js.Function0[Any] = () => {
    flashHandle = Int.MinValue
    peer.classList.remove("pat-button-flash")
  }

  private def flash(): Unit = {
    cancelFlash()
    peer.classList.add("pat-button-flash")
    flashHandle = dom.window.setTimeout(stopFlash, 150)
  }

  private def cancelFlash(): Unit = if (flashHandle != Int.MinValue) {
    dom.window.clearTimeout(flashHandle)
    flashHandle = Int.MinValue
  }

  def dispose(): Unit = {
    elem.removeListener(elemL)
    cancelFlash()
  }

  override protected def init(): Unit = {
    super.init()
    peer.mousePressed { e =>
      if (e.button == 0 && !e.defaultPrevented && isMenu(e)) {  // cause a bang
        elem.inlet ! Message.Bang
        e.preventDefault()
      }
    }
  }
}