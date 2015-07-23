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

  protected def boxWidth = 18

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
    val circleElem = circle(cls := "pat-button", cx := 9.5, cy := 10.5, r := 6.5 /* 8.5 */).render
    peer.appendChild(circleElem)

    peer.mousePressed { e =>
      if (this.isClickAction(e)) {  // cause a bang
        elem.inlet ! Message.Bang
        e.preventDefault()
      }
    }
  }
}