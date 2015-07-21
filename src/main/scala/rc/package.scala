import org.scalajs.dom
import org.scalajs.dom.AudioContext
import rc.audio.AudioContextExt
import rc.sandbox.{AudioSource, AudioSink, HasOut, HasIn}

import scala.language.implicitConversions

package object rc {
  implicit def AudioContextExt(context: AudioContext): AudioContextExt =
    context.asInstanceOf[AudioContextExt]

  // implicit def defaultOut(h: HasOut): AudioSource = h.out

  implicit final class HasOutOps(private val h: HasOut) extends AnyVal {
    def ---> (sink: AudioSink): Unit = h.out ---> sink
    def ---> [B <: HasIn](target: B): target.type = { h.out ---> target.in; target }
  }

  implicit final class SourceOps(private val source: AudioSource) extends AnyVal {
    def ---> [B <: HasIn](target: B): target.type = { source ---> target.in; target }
  }

  val isMac: Boolean = dom.navigator.platform.startsWith("Mac")

  /////////////////////////////

  implicit class PortOps(private val p: Port) extends AnyVal {
    def isInlet   : Boolean = p.isInstanceOf[Inlet ]
    def isOutlet  : Boolean = p.isInstanceOf[Outlet]

    /** Index in the list of inlets or outlets of the node. */
    def index     : Int     = {
      val n     = p.node
      val list  = if (isInlet) n.inlets else n.outlets
      list.indexOf(p)
    }
  }

  implicit class InletOps(private val i: Inlet) extends AnyVal {
    def acceptsMessages   : Boolean = i.accepts(MessageType)
    def acceptsAudio      : Boolean = i.accepts(AudioType  )
  }
}