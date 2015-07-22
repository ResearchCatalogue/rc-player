/*
 *  PatcherImpl.scala
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
package impl

import rc.view.PatcherView

class PatcherImpl extends Patcher with ModelImpl[Patcher.Update] {
  def dispose(): Unit = remove(_elems: _*)

  // {
    // val xs  = _elems
    // _elems  = Vector.empty
    // xs.foreach(_.dispose())
  // }

  def view(): PatcherView = PatcherView(this)

  private var _elems = Vector.empty[Elem]

  def elems: Seq[Elem] = _elems

  def add(elems: Elem*): Unit = {
    _elems ++= elems
    dispatch(Patcher.Added(this, elems: _*))
  }

  def remove(elems: Elem*): Unit = {
    _elems = _elems.diff(elems)
    dispatch(Patcher.Removed(this, elems: _*))
  }
}
