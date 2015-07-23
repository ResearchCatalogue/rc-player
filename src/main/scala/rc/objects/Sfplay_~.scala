package rc
package objects

import org.scalajs.dom
import org.scalajs.dom.{MediaElementAudioSourceNode, AudioNode}
import rc.audio.AudioSystem
import rc.impl.{AudioNodeImpl, ObjNodeImpl, OutletImpl}

import scalatags.JsDom
import scalatags.JsDom.all._

class Sfplay_~(val parent: Patcher, val args: List[Any])
  extends ObjNodeImpl("sfplay~") with AudioNodeImpl { obj =>

  private val mediaElem   = {
    val res = JsDom.tags.audio(cls := "pat-sf").render
    // cf. http://stackoverflow.com/questions/31590108
    res.preload  = "auto"   // needed for Firefox, default in Chromium
    res.controls = true                   // XXX TODO -- testing
    dom.document.body.appendChild(res)
    res
  }

  private var mediaSource = Option.empty[MediaElementAudioSourceNode]

  private var _playing = false
  def playing: Boolean = _playing
  def playing_=(value: Boolean): Unit = if (_playing != value) {
    _playing = value
    if (value) {
      // mediaElem.initialTime = 0.0
    }
    if (mediaSource.isDefined) {
      if (value) {
        mediaElem.play()
      } else {
        mediaElem.pause()
      }
    }
    if (!value) {
      // mediaElem.initialTime = 0.0
    }
  }

  private var _pausing = false
  def pausing: Boolean = _pausing
  def pausing_=(value: Boolean): Unit = if (_pausing != value) {
    _pausing = value
    if (mediaSource.isDefined) {
      if (value) {
        mediaElem.play()
      } else {
        mediaElem.pause()
      }
    }
  }

  private var _speed = 1.0
  def speed: Double = _speed
  def speed_=(value: Double): Unit = if (_speed != value) {
    _speed = value
    mediaElem.playbackRate = value
  }

  private var _url = ""
  def url: String = _url
  def url_=(value: String): Unit = if (_url != value) {
    _url = value
    mediaElem.src = value
    mediaElem.load()
  }

  // XXX TODO --- cannot create media source element twice from same media elem (says Chrome)
  private lazy val lazyM = AudioSystem.context.createMediaElementSource(mediaElem)

  protected def dspStarted(): Unit = if (mediaSource.isEmpty) {
    val m = lazyM // AudioSystem.context.createMediaElementSource(mediaElem)
    if (_playing && !_pausing) mediaElem.play()
    mediaSource = Some(m)
  }

  protected def dspStopped(): Unit = mediaSource.foreach { m =>
    mediaSource = None
    mediaElem.pause()
  }

  def inlets : List[Inlet ] = inlet1  :: inlet2  :: Nil
  def outlets: List[Outlet] = outlet1 :: outlet2 :: Nil

  // http://dev.w3.org/html5/spec-preview/media-elements.html
  private def debug(): Unit = {
    println(s"paused              = ${mediaElem.paused}")
    println(s"ended               = ${mediaElem.ended}")
//    println(s"defaultPlaybackRate = ${mediaElem.ended}")
//    println(s"playbackRate        = ${mediaElem.ended}")
    println(s"played              = ${mediaElem.played}")
    println(s"seeking             = ${mediaElem.seeking}")
    println(s"seekable            = ${mediaElem.seekable}")
//    println(s"volume              = ${mediaElem.volume}")
//    println(s"muted               = ${mediaElem.muted}")
    println(s"readyState          = ${mediaElem.readyState}")
    println(s"buffered            = ${mediaElem.buffered}")
    println(s"preload             = ${mediaElem.preload}")
    println(s"duration            = ${mediaElem.duration}")
    println(s"error               = ${mediaElem.error}")
    println(s"networkState        = ${mediaElem.networkState}")
  }

  val inlet1 = this.messageInlet {
    case M(i: Int) => playing = i != 0
    case M("open", u: String) => url = u
    case M("pause" ) => pausing = true
    case M("resume") => pausing = false
    case M("debug") => debug()
  }

  val inlet2 = this.messageInlet {
    case M(d: Double) => speed = d
  }

  object outlet1 extends OutletImpl {
    def tpe   = AudioType
    def node  = obj

    def audio: AudioNode = mediaSource.getOrElse {
      if (parent.dsp.active) dspStarted()
      mediaSource.getOrElse(throw new Exception("DSP is not active"))
    }
  }

  val outlet2 = this.messageOutlet
}