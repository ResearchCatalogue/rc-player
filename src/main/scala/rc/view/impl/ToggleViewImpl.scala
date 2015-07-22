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
import rc.objects.Toggle

class ToggleViewImpl(val parentView: PatcherView, val elem: Toggle) extends NodeViewImpl {
  val peer: dom.svg.Element = {
    import scalatags.JsDom.all.{width => _, height => _, _}
    import scalatags.JsDom.svgTags._
    import scalatags.JsDom.svgAttrs._
    val loc         = elem.location
    // XXX TODO -- would be great if we could keep these size values in a CSS somehow
    val rectTree    = rect  (cls := "pat-node pat-toggle", x := 0.5, y := 0.5, width := 20, height := 20)
    val line1Tree   = line  (cls := "pat-toggle", x1 := 4.5, y1 :=  4.5, x2 := 16.5, y2 := 16.5)
    val line2Tree   = line  (cls := "pat-toggle", x1 := 4.5, y1 := 16.5, x2 := 16.5, y2 :=  4.5)
    val groupElem   = g     (cls := "pat-node", rectTree, line1Tree, line2Tree,
      transform := s"translate(${loc.x},${loc.y})").render
    groupElem
  }

  init()

  private val elemL = elem.addListener { case _ => updateState() }

  private def updateState(): Unit = {
    val selected = elem.value != 0
    if (selected) peer.classList.add   ("pat-toggle-on")
    else          peer.classList.remove("pat-toggle-on")
  }

  def dispose(): Unit = elem.removeListener(elemL)

  override protected def init(): Unit = {
    super.init()
    peer.mousePressed { e =>
      if (this.isClickAction(e)) {  // cause a toggle
        elem.inlet ! Message(elem.toggleValue)
        e.preventDefault()
      }
    }
    updateState()
  }
}