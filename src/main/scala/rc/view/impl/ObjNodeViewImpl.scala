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

import scalatags.JsDom.all._
import scalatags.JsDom.svgTags

class ObjNodeViewImpl(val parentView: PatcherView, val elem: ObjNode) extends NodeViewImpl {
  val peer: dom.svg.Element = {
    import svgTags._
    val textTree  = text(cls := "pat-node-name", elem.name)
    val rectTree  = rect(cls := "pat-node")
    val loc       = elem.location
    val groupElem = g(cls := "pat-node", textTree, rectTree, left := loc.x, top := loc.y).render
    groupElem
  }

  def dispose(): Unit = ()
}
