/*
 *  Receive.scala
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

import rc.impl.{NoInlets, ObjNodeImpl, SingleOutlet}

class Receive(val parent: Patcher, val args: List[Any])
  extends ObjNodeImpl("receive") with NoInlets with SingleOutlet {

  private val portName = args match {
    case (n: String) :: Nil => n
    case _ => throw new IllegalArgumentException(s"'receive' requires one string argument, the port name")
  }

  val outlet = this.messageOutlet

  private val inlet = Registry.addReceive(portName, { m: M => outlet(m) })

  override def dispose(): Unit = {
    super.dispose()
    Registry.removeReceive(portName, inlet)
  }
}