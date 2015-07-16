/*
 *  Player.scala
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

package rc

import org.scalajs.dom.raw.HTMLMediaElement
import org.scalajs.jquery.{jQuery => $}

import scala.scalajs.js

object Player extends js.JSApp {
  def main(): Unit = $(documentLoaded _)

  private def documentLoaded(): Unit = {
    val but = new PlayButton
    $("#player").append(but.render)

    implicit val as = new AudioSystem
    val noise = new WhiteNoise
    val gain  = new Gain(0.05)
    val dac   = new DAC
    noise ---> gain ---> dac

    $("#sound-file")(0) match {
      case m: HTMLMediaElement =>
        val disk = new DiskIn(m)
        disk ---> dac

        m.play()

      case _ =>
        // XXX TODO --- emit warning
    }
  }
}
