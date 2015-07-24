/*
 *  package.scala
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

package object impl {
  implicit class NodeImplOps(private val node: Node) extends AnyVal {
    def messageInlet(fun: M => Unit): Inlet =
      new MessageInletImpl(node, fun)

    def messageOutlet: Outlet with (M => Unit) =
      new MessageOutletImpl(node)
  }
}
