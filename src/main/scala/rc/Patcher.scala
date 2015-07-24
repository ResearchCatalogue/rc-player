/*
 *  Patcher.scala
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

package rc

import rc.impl.PatcherImpl
import rc.view.PatcherView

object Patcher {
  sealed trait Update { def patcher: Patcher }
  case class Added  (patcher: Patcher, elems: Elem*) extends Update
  case class Removed(patcher: Patcher, elems: Elem*) extends Update

  def apply(): Patcher = new PatcherImpl
}
trait Patcher extends Model[Patcher.Update] {
  def dispose(): Unit
  def view(): PatcherView

  def add   (elems: Elem*): Unit
  def remove(elems: Elem*): Unit

  def elems: Seq[Elem]

  def dsp: DSPStatus
}

trait DSPStatus extends Model[Boolean] {
  var active: Boolean
}