package rc
package objects

import rc.impl.{ObjNodeImpl, SingleOutlet}

object Random {
  private final val multiplier  = 0x5DEECE66DL
  private final val mask        = (1L << 48) - 1
  private final val addend      = 11L

  private def initialScramble(seed: Long): Long = (seed ^ multiplier) & mask
}
class Random(val parent: Patcher, val args: List[Any] = Nil)
  extends ObjNodeImpl("random") with SingleOutlet {

  import Random.{multiplier, addend, mask, initialScramble}

  private var seed : Long = _
  private var range: Int  = 1

  val outlet = this.messageOutlet("Random Numbers between 0 and range-1")

  val inlet1 = this.messageInlet("Bang to Generate Number") {
    case Message.Bang => outlet(Message(nextInt(range)))
    case Message("seed", value: Int) => seed = initialScramble(value)
  }

  val inlet2 = this.messageInlet("Sets Range") {
    case Message(value: Int) =>
      require(range > 0, "range must be positive")
      range = value
  }

  init()

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