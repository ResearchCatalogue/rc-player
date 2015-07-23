/*
 *  Route.scala
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

import rc.impl.{ObjNodeImpl, SingleInlet, NodeImplOps}

trait RouteLike extends ObjNode with SingleInlet {
  val outlets = List.fill(args.size + 1)(this.messageOutlet)
  private val outRest = outlets.last

  val inlet = this.messageInlet { m =>
    // XXX TODO -- an empty message is illogical; should forbid that case
    val a = m.atoms
    val i = args.indexOf(a.head)
    if (i >= 0) outlets(i)(stripMatch(m)) else outRest(m)
  }

  protected def stripMatch(in: M): M
}

class Route(val parent: Patcher, val args: List[Any])
  extends ObjNodeImpl("route") with RouteLike {

  protected def stripMatch(in: M): M = M(in.atoms.tail)
}

class RoutePass(val parent: Patcher, val args: List[Any])
  extends ObjNodeImpl("routepass") with RouteLike {

  protected def stripMatch(in: M): M = in
}