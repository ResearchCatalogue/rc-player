/*
 *  ObjNodeViewImpl.scala
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

class ObjNodeViewImpl(val parentView: PatcherView, val elem: ObjNode) extends NodeViewImpl {
  val peer: dom.svg.Element = {
    import scalatags.JsDom.all.{width => _, height => _, _}
    import scalatags.JsDom.svgTags._
    import scalatags.JsDom.svgAttrs._
    val elemText  = elem.contents
    val textWidth = elemText.length * 7 + 8 // 2 // 4
    val textTree  = text(cls := "pat-node-name", x := 4, y := 15, elemText)
    val rectTree  = rect(cls := "pat-node", x := 0.5, y := 0.5, width := textWidth, height := 20)
    val loc       = elem.location
    val groupElem = g(cls := "pat-node", rectTree, textTree, transform := s"translate(${loc.x},${loc.y})").render
    groupElem
  }

  init()

  override def toString = s"View of $elem"

  def dispose(): Unit = ()
}
