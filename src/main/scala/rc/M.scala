/*
 *  M.scala
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

import scala.annotation.meta.field
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

object M {
  val Bang = M(rc.Bang)
}
@JSExport("rc.M")
case class M(atoms: Any*) {
  @JSExport("atoms") def toArray: js.Array[Any] = atoms.to[js.Array]

  override def toString = atoms.mkString(s"$productPrefix(", ", ", ")")
}