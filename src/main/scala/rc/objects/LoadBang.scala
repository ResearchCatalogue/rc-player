/*
 *  LoadBang.scala
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

import rc.impl.{ModelImpl, NoArgs, ObjNodeImpl, SingleInlet, SingleOutlet}

class LoadBang(val parent: Patcher)
  extends ObjNodeImpl("loadbang")
  with ModelImpl[Unit]
  with SingleInlet with SingleOutlet with NoArgs {

  val outlet = this.messageOutlet

  private val bangL = parent.addListener {
    case Patcher.Loaded(_) => outlet(M.Bang)
  }

  val inlet = this.messageInlet {
    case M.Bang => outlet(M.Bang)
  }

  override def dispose(): Unit = {
    super.dispose()
    parent.removeListener(bangL)
  }
}