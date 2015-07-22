/*
 *  PatcherSelection.scala
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

import rc.view.impl.PatcherSelectionImpl

object PatcherSelection {
  type Listener = Model.Listener[Update]

  sealed trait Update { def patcher: PatcherView }
  case class Added  (patcher: PatcherView, elems: View*) extends Update
  case class Removed(patcher: PatcherView, elems: View*) extends Update

  def apply(patcher: PatcherView): PatcherSelection = new PatcherSelectionImpl(patcher)
}
trait PatcherSelection extends Model[PatcherSelection.Update] {

  def contains(elem: View): Boolean

  def clear(): Unit

  def add   (elems: View*): Unit
  def remove(elems: View*): Unit

  def size: Int

  def isEmpty : Boolean
  def nonEmpty: Boolean

  def get: Set[View]
}
