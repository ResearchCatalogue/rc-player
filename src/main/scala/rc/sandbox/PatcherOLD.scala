/*
 *  Patcher.scala
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

package rc.sandbox

import org.scalajs.dom
import rc.view.IntSize2D

object PatcherOLD {
  def apply(): PatcherOLD = new PatcherOLDImpl
}
trait PatcherOLD extends Widget {
  def cableElement: dom.svg.SVG

  def size: IntSize2D

  def selection: PatcherSelection
}