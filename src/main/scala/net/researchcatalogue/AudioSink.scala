package net.researchcatalogue

import org.scalajs.dom.AudioNode

sealed trait AudioPort {
  def node: AudioNode
}

trait AudioSink extends AudioPort {
  def add   (source: AudioSource): Unit
  def remove(source: AudioSource): Unit
}

trait AudioSource extends AudioPort {
  def add   (sink: AudioSink): Unit
  def remove(sink: AudioSink): Unit
}