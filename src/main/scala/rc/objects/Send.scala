/*
 *  Send.scala
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

import rc.impl.{NoOutlets, ObjNodeImpl, SingleInlet}

class Send(val parent: Patcher, val args: List[Any])
  extends ObjNodeImpl("send") with SingleInlet with NoOutlets {

  private val portName = args match {
    case (n: String) :: Nil => n
    case _ => throw new IllegalArgumentException(s"'send' requires one string argument, the port name")
  }

  private val outlet = Registry.addSend(portName)

  val inlet = this.messageInlet(outlet(_))

  override def dispose(): Unit = {
    super.dispose()
    Registry.removeSend(portName, outlet)
  }
}