/*
 *  Button.scala
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

import rc.impl.{ModelImpl, NoArgs, SingleOutlet, SingleInlet, ObjNodeImpl}
import rc.view.{ButtonView, NodeView, PatcherView}

class Button(val parent: Patcher)
  extends ObjNodeImpl("button")
  with ModelImpl[Unit]
  with SingleInlet with SingleOutlet with NoArgs {

  override def view(parentView: PatcherView): NodeView = ButtonView(parentView, this)

  val outlet = this.messageOutlet("Bang Messages")

  val inlet = this.messageInlet("Any Message Triggers a Bang") { _ =>
    dispatch(())
    outlet.apply(Message.Bang)
  }
}