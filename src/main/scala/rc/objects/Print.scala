/*
 *  Print.scala
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

class Print(val parent: Patcher, val args: List[Any] = Nil)
  extends ObjNodeImpl("print") with SingleInlet with NoOutlets {

  private val prefix = args match {
    case Nil => "print: "
    case "-n" :: Nil => ""
    case _ => args.mkString("", " ", ": ")
  }

  val inlet = this.messageInlet { message =>
    val m = message.atoms.mkString(prefix, " ", "")
    println(m)
  }
}
