package rc

trait HasIn {
  def in: AudioSink
}

trait HasOut {
  def out: AudioSource
}

trait Filter extends HasIn with HasOut

trait Generator extends HasOut