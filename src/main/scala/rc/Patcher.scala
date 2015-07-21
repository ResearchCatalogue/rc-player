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

package rc

import org.scalajs.dom
import rc.impl.PatcherImpl

object Patcher {
  def apply(): Patcher = new PatcherImpl
}
trait Patcher extends Widget {
  def cableElement: dom.svg.SVG

  def size: IntSize2D

  def selection: PatcherSelection
}