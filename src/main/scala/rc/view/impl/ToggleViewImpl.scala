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

import rc.objects.Toggle

class ToggleViewImpl(val parentView: PatcherView, val elem: Toggle) extends RectNodeViewImpl {

  private val elemL = elem.addListener { case _ => updateState() }

  init()

  protected def boxWidth = 18

  def dispose(): Unit = elem.removeListener(elemL)

  override protected def init(): Unit = {
    super.init()

    import scalatags.JsDom.all.{height => _, width => _, _}
    import scalatags.JsDom.svgAttrs._
    import scalatags.JsDom.svgTags._
    val line1Elem = line  (cls := "pat-toggle", x1 := 4.5, y1 :=  5.5, x2 := 14.5, y2 := 15.5).render
    val line2Elem = line  (cls := "pat-toggle", x1 := 4.5, y1 := 15.5, x2 := 14.5, y2 :=  5.5).render
    peer.appendChild(line1Elem)
    peer.appendChild(line2Elem)

    peer.mousePressed { e =>
      if (this.isClickAction(e)) {  // cause a toggle
        elem.inlet ! Message(elem.toggleValue)
        e.preventDefault()
      }
    }
    updateState()
  }

  private def updateState(): Unit = {
    val selected = elem.value != 0
    if (selected) peer.classList.add   ("pat-toggle-on")
    else          peer.classList.remove("pat-toggle-on")
  }
}