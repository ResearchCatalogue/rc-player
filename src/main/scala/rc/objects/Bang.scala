/*
 *  Bang.scala
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

import rc.impl.{MessageInletImpl, MessageOutletImpl, ObjNodeImpl}
import rc.view.{BangView, NodeView, PatcherView}

class Bang(val parent: Patcher) extends ObjNodeImpl { obj =>
  def name = "bang"

  def inlets : List[Inlet ] = _inlet :: Nil
  def outlets: List[Outlet] = _outlet :: Nil

  def dispose(): Unit = ()

  override def view(parentView: PatcherView): NodeView = BangView(parentView, this)

  def inlet : Inlet   = _inlet
  def outlet: Outlet  = _outlet

  private object _inlet extends MessageInletImpl {
    def description: String = "Any Message Triggers a Bang"

    def node: Node = obj

    def ! (message: Message): Unit = _outlet.dispatch(Message.Bang)
  }

  private val _outlet = new MessageOutletImpl(this, "Bang Messages")
}
