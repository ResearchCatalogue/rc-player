/*
 *  Point2D.scala
 *  (rc-player)
 *
 *  Copyright (c) 2015 Society of Artistic Research (SAR). All rights reserved.
 *  Written by Hanns Holger Rutz.
 *
 *	This software is published under a BSD 2-Clause License.
 *
 *
 *	For further information, please contact Hanns Holger Rutz at
 *	contact@sciss.de
 */

package rc.view

import scala.language.implicitConversions

object IntPoint2D {
  implicit def fromTuple(tup: (Int, Int)): IntPoint2D = IntPoint2D(tup._1, tup._2)
}
case class IntPoint2D(x: Int, y: Int) {
  def + (that: IntPoint2D): IntPoint2D = IntPoint2D(this.x + that.x, this.y + that.y)
}

case class IntSize2D(width: Int, height: Int)

case class DoublePoint2D(x: Double, y: Double)