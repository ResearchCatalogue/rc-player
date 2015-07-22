/*
 *  Multiply_~.scala
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

import rc.impl.{ObjNodeImpl, SingleOutlet}

class Multiply_~(val parent: Patcher, val args: List[Any])
  extends ObjNodeImpl("*~") with SingleOutlet {

  val outlet: Outlet = this.messageOutlet("Multiplied signal")

  val inlet1 = this.messageInlet("Left operand (audio)") { message =>
    println("TODO")
  }

  val inlet2 = this.messageInlet("Right operand (audio or message)") { message =>
    println("TODO")
  }

  def inlets: List[Inlet] = inlet1 :: inlet2 :: Nil
}
