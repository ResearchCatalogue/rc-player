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

import rc.impl.CordImpl
import rc.view.{PatcherView, View}

sealed trait Elem extends Disposable {
  def parent: Patcher
  def view(parentView: PatcherView): View
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

object Cord {
  def apply(source: Outlet, sink: Inlet): Cord = new CordImpl(source, sink)
}
/* sealed */ trait Cord extends Elem {
  def source: Outlet
  def sink  : Inlet
  def tpe   : Type
}

//object MessageCord {
//  def apply(source: Outlet, sink: Inlet): MessageCord = ...
//}
//trait MessageCord extends Cord {
//  def tpe = MessageType
//}
//
//object AudioCord {
//  def apply(source: Outlet, sink: Inlet): AudioCord = ...
//}
//trait AudioCord extends Cord {
//  def tpe = AudioType
//}
