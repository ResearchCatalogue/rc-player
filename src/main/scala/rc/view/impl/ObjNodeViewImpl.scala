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

class ObjNodeViewImpl(val parentView: PatcherView, val elem: ObjNode, elemText: String)
  extends RectNodeViewImpl {

  init()

  protected def boxWidth: Int = elemText.length * 7 + 8

  override def toString = s"View of $elem"

  override protected def init(): Unit = {
    super.init()

    import scalatags.JsDom.all._
    import scalatags.JsDom.svgAttrs._
    import scalatags.JsDom.svgTags._
    val textTree = text(cls := "pat-node-name", x := 4, y := 15, elemText).render
    peer.appendChild(textTree)
  }

  def dispose(): Unit = ()
}
