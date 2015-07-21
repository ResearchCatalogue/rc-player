/*
 *  IntPoint2D.scala
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

package rc.view

case class IntPoint2D(x: Int, y: Int) {
  def + (that: IntPoint2D): IntPoint2D = IntPoint2D(this.x + that.x, this.y + that.y)
}

case class IntSize2D(width: Int, height: Int)