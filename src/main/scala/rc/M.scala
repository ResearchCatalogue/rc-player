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

object M {
  val Bang = M(rc.Bang)
}
case class M(atoms: Any*) {
  override def toString = atoms.mkString(s"$productPrefix(", ", ", ")")
}