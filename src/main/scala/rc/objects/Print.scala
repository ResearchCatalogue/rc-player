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

import rc.impl.{InletImpl, ModelImpl, ObjNodeImpl}

class Print(val parent: Patcher, prefix: String) extends ObjNodeImpl { obj =>
  /** Default prefix is `"print"` */
  def this(parent: Patcher) = this(parent, prefix = "print: ")

  def this(parent: Patcher, args: List[String]) = this(parent, {
    args match {
      case "-n" :: Nil => ""
      case _ => args.mkString("", " ", ": ")
    }
  })

  def name = "print"

  def inlets : List[Inlet ] = _inlet :: Nil
  def outlets: List[Outlet] = Nil

  def inlet: Inlet = _inlet

  def dispose(): Unit = ()

  private object _inlet extends InletImpl with ModelImpl[Port.Update] {
    def description: String = "Messages to Print"

    def node: Node = obj

    def accepts(tpe: Type): Boolean = tpe == MessageType

    def ! (message: Message): Unit = {
      val m = message.atoms.mkString(prefix, " ", "")
      println(m)
    }
  }
}
