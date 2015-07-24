/*
 *  Random.scala
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

import rc.impl.{ObjNodeImpl, SingleOutlet}

class Random(val parent: Patcher, val args: List[Any] = Nil)
  extends ObjNodeImpl("random") with SingleOutlet {

  private final val multiplier  = 0x5DEECE66DL
  private final val mask        = (1L << 48) - 1
  private final val addend      = 11L

  private var seed : Long = _
  private var range: Int  = 1

  val outlet = this.messageOutlet

  val inlet1 = this.messageInlet {
    case M.Bang => outlet(M(nextInt(range)))
    case M("seed", value: Int) => seed = initialScramble(value)
  }

  val inlet2 = this.messageInlet {
    case M(value: Int) =>
      require(range > 0, "range must be positive")
      range = value
  }

  init()

  private def initialScramble(seed: Long): Long = (seed ^ multiplier) & mask

  private def next(bits: Int): Int = {
    val oldSeed = seed
    val nextSeed = (oldSeed * multiplier + addend) & mask
    seed = nextSeed
    (nextSeed >>> (48 - bits)).toInt
  }

  private def nextInt(n: Int): Int = {
    if ((n & -n) == n) {
      // n is a power of 2
      return ((n * next(31).toLong) >> 31).toInt
    }
    while (true) {
      val bits = next(31)
      val res = bits % n
      if (bits - res + n >= 1) return res
    }
    sys.error("Never here")
  }

  def inlets = inlet1 :: inlet2 :: Nil

  private def init(): Unit = {
    val hasSeed = false
    args match {
      case (value: Int) :: tail =>
        range = value
        tail match {
          case (value: Int) :: Nil =>
            seed = initialScramble(value)
          case Nil =>
          case _ => bail()
        }
      case Nil =>
      case _ => bail()
    }
    if (!hasSeed) seed = initialScramble(System.currentTimeMillis())
  }

  private def bail(): Unit =
    Console.err.println(s"Illegal arguments for $name")
}