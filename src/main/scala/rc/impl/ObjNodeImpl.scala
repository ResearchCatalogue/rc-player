/*
 *  ObjNodeImpl.scala
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

package rc.impl

import rc.ObjNode
import rc.view.{NodeView, PatcherView}

abstract class ObjNodeImpl(val name: String) extends NodeImpl with ObjNode {
  /** The default implementation calls `View(this)` */
  def view(parentView: PatcherView): NodeView = NodeView(parentView, this)

  override def toString = s"$name@${hashCode.toHexString}"

  def contents: String = if (args.isEmpty) name else (name :: args).mkString(" ")
}