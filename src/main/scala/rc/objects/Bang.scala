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

import rc.impl.{ObjNodeImpl, MessageInletImpl, MessageOutletImpl}
import rc.view.{BangView, View}

class Bang(val parent: Patcher) extends ObjNodeImpl { obj =>
  def name = "bang"

  def inlets : List[Inlet ] = inlet  :: Nil
  def outlets: List[Outlet] = outlet :: Nil

  def dispose(): Unit = ()

  override def view(): View = BangView(this)

  private object inlet extends MessageInletImpl {
    def description: String = "Any Message Triggers a Bang"

    def node: Node = obj

    def ! (message: Message): Unit = outlet.dispatch(Message.Bang)
  }

  private val outlet = new MessageOutletImpl(this, "Bang Messages")
}
