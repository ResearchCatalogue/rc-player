/*
 *  Elem.scala
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

import rc.view.View

sealed trait Elem extends Disposable {
  def parent: Patcher
  def view(): View
}

sealed trait Node extends Elem {
  def inlets : List[Inlet ]
  def outlets: List[Outlet]
  def state  : State
}

trait MessageNode extends Node {
  var content: Message
}

trait ObjNode extends Node {
  def name: String
}

trait Cord extends Elem {
  def source: Outlet
  def sink  : Inlet
  def tpe   : Type
}
