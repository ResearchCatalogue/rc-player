/*
 *  Toggle.scala
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
package objects

import rc.impl.{ModelImpl, NoArgs, SingleOutlet, SingleInlet, ObjNodeImpl}
import rc.view.{ToggleView, NodeView, PatcherView}

class Toggle(val parent: Patcher)
  extends ObjNodeImpl("toggle")
  with ModelImpl[Int]
  with SingleInlet with SingleOutlet with NoArgs {

  override def view(parentView: PatcherView): NodeView = ToggleView(parentView, this)

  val outlet = this.messageOutlet

  private var _state = 0

  def value: Int = _state
  def value_=(i: Int): Unit = if (_state != i) {
    _state = i
    dispatch(i)
  }

  def toggleValue: Int = if (_state == 0) 1 else 0

  val inlet = this.messageInlet {
    case M.Bang =>
      value = toggleValue
      flush()
    case M(d: Double) =>
      value = d.toInt
      flush()
    case M("set", i: Int) =>
      value = i
  }

  private def flush(): Unit = outlet(M(_state))
}