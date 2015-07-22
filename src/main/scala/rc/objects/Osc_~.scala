/*
 *  Osc_~.scala
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

import rc.impl.{ObjNodeImpl, SingleInlet, SingleOutlet}

class Osc_~(val parent: Patcher, val args: List[Any])
  extends ObjNodeImpl("osc~") with SingleInlet with SingleOutlet {

  val outlet: Outlet = this.messageOutlet("Oscillator signal")

  val inlet = this.messageInlet("Frequency in Hertz") { message =>
    println("TODO")
  }
}
