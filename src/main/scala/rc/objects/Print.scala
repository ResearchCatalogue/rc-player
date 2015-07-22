/*
 *  Print.scala
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

import rc.impl.{NoOutlets, ObjNodeImpl, SingleInlet}

class Print(val parent: Patcher, prefix: String) extends ObjNodeImpl("print") with SingleInlet with NoOutlets {
  /** Default prefix is `"print"` */
  def this(parent: Patcher) = this(parent, prefix = "print: ")

  def this(parent: Patcher, args: List[String]) = this(parent, {
    args match {
      case "-n" :: Nil => ""
      case _ => args.mkString("", " ", ": ")
    }
  })

  val inlet = this.messageInlet("Messages to Print") { message =>
    val m = message.atoms.mkString(prefix, " ", "")
    println(m)
  }
}
