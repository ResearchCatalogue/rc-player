/*
 *  PatcherSelection.scala
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
package view

import rc.view.impl.PatcherSelectionImpl

object PatcherSelection {
  type Listener = Model.Listener[Update]

  sealed trait Update { def patcherView: PatcherView }
  case class Added  (patcherView: PatcherView, views: View*) extends Update
  case class Removed(patcherView: PatcherView, views: View*) extends Update

  def apply(patcherView: PatcherView): PatcherSelection = new PatcherSelectionImpl(patcherView)
}
trait PatcherSelection extends Model[PatcherSelection.Update] {

  def contains(view: View): Boolean

  def clear(): Unit

  def add   (views: View*): Unit
  def remove(views: View*): Unit

  def size: Int

  def isEmpty : Boolean
  def nonEmpty: Boolean

  def get: Set[View]
}
