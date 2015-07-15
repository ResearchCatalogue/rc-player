package net

import org.scalajs.dom.AudioContext

import scala.language.implicitConversions

package object researchcatalogue {
  implicit def AudioContextExt(context: AudioContext): AudioContextExt =
    context.asInstanceOf[AudioContextExt]
}