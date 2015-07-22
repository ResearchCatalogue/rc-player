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
import rc.objects.Bang

class BangViewImpl(val elem: Bang) extends View {
  val peer: dom.svg.Element = {
    import scalatags.JsDom.all.{width => _, height => _, _}
    import scalatags.JsDom.svgTags._
    import scalatags.JsDom.svgAttrs._
    val loc         = elem.location
    val rectTree    = rect  (cls := "pat-node pat-bang", x := 0.5, y := 0.5, width := 20, height := 20)
    val circleTree  = circle(cls := "pat-bang", cx := 10.5, cy := 10.5, r := 7.5 /* 8.5 */)
    val groupElem   = g(cls := "pat-node", rectTree, circleTree, transform := s"translate(${loc.x},${loc.y})").render
    groupElem
  }

  def dispose(): Unit = ()
}