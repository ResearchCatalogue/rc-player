import org.scalajs.dom.AudioContext

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
}