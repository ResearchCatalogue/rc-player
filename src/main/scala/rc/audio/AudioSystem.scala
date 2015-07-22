/*
 *  AudioSystem.scala
 *  (rc-player)
 *
 *  Copyright (c) 2015 Society of Artistic Research (SAR). All rights reserved.
 *  Written by Hanns Holger Rutz.
 *
 *	This software is published under the GNU General Public License v3+
 *
 *
 *	For further information, please contact Hanns Holger Rutz at
 *	contact@sciss.de
 */

package rc.audio

import org.scalajs.dom.raw.AudioContext

object AudioSystem {
  lazy val instance: AudioSystem = new AudioSystem

  def context: AudioContext = instance.context
}
class AudioSystem {
  val context = new AudioContext
}
