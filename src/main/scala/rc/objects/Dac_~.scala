/*
 *  Dac_~.scala
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

import rc.impl.{NoArgs, NoOutlets, ObjNodeImpl, SingleInlet}

class Dac_~(val parent: Patcher)
  extends ObjNodeImpl("dac~") with SingleInlet with NoOutlets with NoArgs {

  val inlet = this.messageInlet("Start/stop message and audio input") { message =>
    println("TODO")
  }
}
