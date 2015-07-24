/*
 *  Box.scala
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

package rc.sandbox

import rc.view.{IntPoint2D, IntSize2D}

object Box {
  def apply(patcher: PatcherOLD): Box = new BoxImpl(patcher)
}
trait Box extends Widget {
  def patcher: PatcherOLD

  var location: IntPoint2D
  def size: IntSize2D

  def focus(): Unit

  def numInlets : Int
  def numOutlets: Int

  def dispose(): Unit
}