/*
 *  PatcherImpl.scala
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
package impl

import rc.view.PatcherView

class PatcherImpl extends Patcher with ModelImpl[Patcher.Update] {

  private var _elems = Vector.empty[Elem] // XXX TODO -- perhaps a Queue (fast append) would suffice

  def dispose(): Unit = remove(_elems: _*)

  object dsp extends DSPStatus with ModelImpl[Boolean] {
    private var _active = false

    def active: Boolean = _active
    def active_=(value: Boolean): Unit = if (_active != value) {
      // important to store the new state before dispatching,
      // because connecting nodes might reach out for those
      // later in the listener sequence which in turn might
      // query the status.
      _active = value
      dispatch(value)
    }
  }

  def view(): PatcherView = PatcherView(this)

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