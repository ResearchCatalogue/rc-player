package rc
package objects

import rc.impl.{NoArgs, SingleInlet, ObjNodeImpl, SingleOutlet}

class Mtof(val parent: Patcher)
  extends ObjNodeImpl("mtof") with SingleInlet with SingleOutlet with NoArgs {

  private val /* var */ base = 440.0

  val outlet = this.messageOutlet("Frequency in Hertz")

  val inlet = this.messageInlet("MIDI note number(s)") { m =>
    val freq = m.atoms.map {
      case d: Double => cpsmidi(d)
    }
    outlet(Message(freq: _*))
  }

  private def cpsmidi(in: Double): Double = base * math.pow(2, (in - 69) / 12)
}