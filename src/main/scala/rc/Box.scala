/*
 *  Box.scala
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

import rc.impl.BoxImpl

object Box {
  def apply(): Box = new BoxImpl
}
trait Box extends Widget {
  var loc: IntPoint2D
  def focus(): Unit
}