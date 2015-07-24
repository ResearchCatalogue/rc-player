/*
 *  Mtof.scala
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

import rc.impl.{NoArgs, SingleInlet, ObjNodeImpl, SingleOutlet}

class Mtof(val parent: Patcher)
  extends ObjNodeImpl("mtof") with SingleInlet with SingleOutlet with NoArgs {

  private val /* var */ base = 440.0

  val outlet = this.messageOutlet

  val inlet = this.messageInlet { m =>
    val freq = m.atoms.map {
      case d: Double => cpsmidi(d)
    }
    outlet(M(freq: _*))
  }

  private def cpsmidi(in: Double): Double = base * math.pow(2, (in - 69) / 12)
}