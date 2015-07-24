/*
 *  PatcherView.scala
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

import org.scalajs.dom
import rc.view.impl.PatcherViewImpl

object PatcherView {
  def apply(patcher: Patcher): PatcherView = new PatcherViewImpl(patcher)

  sealed trait Update { def patcher: PatcherView }
  case class ModeChanged(patcher: PatcherView, editing: Boolean) extends Update
  case class Added      (patcher: PatcherView, elems: View*) extends Update
  case class Removed    (patcher: PatcherView, elems: View*) extends Update
}
trait PatcherView extends /* View with */ Model[PatcherView.Update] {
  def patcher: Patcher

  def container: dom.html.Element

  /** Whether the view is in editing mode (`true`) or performance mode (`false`). */
  var editing: Boolean

  def getView(elem: Elem): Option[View]

  def selection: PatcherSelection
}
