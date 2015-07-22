/*
 *  Toggle.scala
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
package objects

import rc.impl.{NoArgs, SingleOutlet, SingleInlet, ObjNodeImpl}
import rc.view.{ToggleView, NodeView, PatcherView}

class Toggle(val parent: Patcher) extends ObjNodeImpl("toggle") with SingleInlet with SingleOutlet with NoArgs {
  override def view(parentView: PatcherView): NodeView = ToggleView(parentView, this)

  val outlet = this.messageOutlet("0 and 1, or Input Int")

  private var _state = 0

  def value: Int = _state

  def toggleValue: Int = if (_state == 0) 1 else 0

  val inlet = this.messageInlet("Bang to toggle or Int to set state") {
    case Message.Bang =>
      _state = toggleValue
      flush()
    case Message(i: Int) =>
      _state = i
      flush()
    case Message(f: Float) =>
      _state = f.toInt
      flush()
    case Message("set", i: Int) =>
      _state = i
  }

  private def flush(): Unit = outlet(Message(_state))
}