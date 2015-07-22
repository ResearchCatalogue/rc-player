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
import rc.objects.{Bang, Print}
import rc.sandbox.{DiskIn, Meter, PatcherOLD, PhysicalOut, PlayButton}

import scala.scalajs.js

object Player extends js.JSApp {
  def main(): Unit = $(documentLoaded _)

  private def documentLoaded(): Unit = {
    val patcher     = Patcher()
    val bang        = new Bang(patcher)
    bang.location   = (40, 40)
    patcher.add(bang)
    val print       = new Print(patcher)
    print.location  = (40, 100)
    patcher.add(print)
    bang.outlet ---> print.inlet

    $("body").append(patcher.view().container)
  }

  private def old(): Unit = {
    val but = new PlayButton
    $("#player").append(but.render)

    // implicit val as = new AudioSystem
    val dac = new PhysicalOut
//    val noise = new WhiteNoise
//    val gain  = new Gain(0.05)
//    noise ---> gain ---> dac

    $("#sound-file")(0) match {
      case m: HTMLMediaElement =>
        val disk = new DiskIn(m)
        disk ---> dac

        // m.play()

        val meter = new Meter
        disk ---> meter

        $("#meter").append(meter.render)

//        $("#meter").click { e: JQueryEventObject =>
//          val peak = meter.peak
//          val rms  = meter.rms
//          meter.reset()
//          println(s"peak = $peak, rms = $rms")
//        }

      case _ =>
        // XXX TODO --- emit warning
    }

    $("body").append(PatcherOLD().render)
  }
}
