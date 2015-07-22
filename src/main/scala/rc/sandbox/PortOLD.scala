/*
 *  Port.scala
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
package sandbox

import rc.view.{IntPoint2D, IntSize2D}

object PortOLD {
  def apply(box: Box, isInlet: Boolean, index: Int): PortOLD = new PortOLDImpl(box = box, isInlet = isInlet, index = index)
}
trait PortOLD extends Widget {
  def box: Box
  def isInlet: Boolean
  def index: Int

  def location: IntPoint2D
  def size: IntSize2D

  def dispose(): Unit
}
