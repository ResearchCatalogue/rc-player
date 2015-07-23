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
import rc.sandbox.{DiskIn, Meter, PatcherOLD, PhysicalOut, PlayButton}

import scala.scalajs.js

object Player extends js.JSApp {
  def main(): Unit = $(documentLoaded _)

  private def documentLoaded(): Unit = {
    val patcher     = Patcher()
    val bRandom     = new objects.Button(patcher)
    patcher add (40, 40) -> bRandom
    val random      = new objects.Random(patcher, 128 :: Nil)
    patcher add (40, 80) -> random
    val mtof        = new objects.Mtof(patcher)
    patcher add (40, 240) -> mtof
    val print       = new objects.Print(patcher)
    patcher add (40, 280) -> print

    val tDac        = new objects.Toggle(patcher)
    patcher add (340, 280) -> tDac
    val dac         = new objects.Dac_~(patcher)
    patcher add (280, 280) -> dac
    val osc         = new objects.Osc_~(patcher, 1000 :: Nil)
    patcher add (160, 280) -> osc
    val gain        = new objects.Multiply_~(patcher, 0.2 :: Nil)
    patcher add (160, 320) -> gain
    val mGain0      = new objects.Message(patcher, 0.0 :: Nil)
    patcher add (240, 320) -> mGain0
    val mGain02     = new objects.Message(patcher, 0.2 :: Nil)
    patcher add (280, 320) -> mGain02

    val m441        = new objects.Message(patcher, 448 :: Nil)
    patcher add (160, 240) -> m441

    val sf          = new objects.Sfplay_~(patcher)
    patcher add (160, 80) -> sf
    val mOpen       = new objects.Message(patcher, "open" :: "/noises2/staircase.mp3" :: Nil)
    patcher add (220, 40) -> mOpen
    val tSf         = new objects.Toggle(patcher)
    patcher add (120, 40) -> tSf
    val mDebug      = new objects.Message(patcher, "debug" :: Nil)
    patcher add (160, 40) -> mDebug
    val route       = new objects.Route(patcher, "time" :: "buffered" :: "duration" :: "ended" :: Nil)
    patcher add (240, 80) -> route
    val msTime      = new objects.Message(patcher, "set" :: "$1" :: Nil)
    patcher add (240, 120) -> msTime
    val mrTime      = new objects.Message(patcher, Nil)
    patcher add (240, 160) -> mrTime
    val msDur       = new objects.Message(patcher, "set" :: "$1" :: Nil)
    patcher add (320, 120) -> msDur
    val mrDur       = new objects.Message(patcher, Nil)
    patcher add (320, 160) -> mrDur
    val bEnded      = new objects.Button(patcher)
    patcher add (400, 120) -> bEnded
    val printSf     = new objects.Print(patcher, "sfplay~" :: Nil)
    patcher add (440, 120) -> printSf

    val tSfVol        = new objects.Toggle(patcher)
    patcher add (340, 240) -> tSfVol
    val gainSf        = new objects.Multiply_~(patcher, 1 :: Nil)
    patcher add (280, 240) -> gainSf

    $("body").append(patcher.view().container)

    bRandom .outlet   ---> random .inlet1
    random  .outlet   ---> mtof   .inlet
    mtof    .outlet   ---> print  .inlet
    mtof    .outlet   ---> osc    .inlet
    osc     .outlet   ---> gain   .inlet1
    gain    .outlet   ---> dac    .inlet
    tDac    .outlet   ---> dac    .inlet
    m441    .outlet   ---> osc    .inlet
    mGain0  .outlet   ---> gain   .inlet2
    mGain02 .outlet   ---> gain   .inlet2

    mOpen   .outlet   ---> sf     .inlet1
    tSf     .outlet   ---> sf     .inlet1
    mDebug  .outlet   ---> sf     .inlet1
    sf      .outlet1  ---> gainSf .inlet1
    sf      .outlet2  ---> route  .inlet
    gainSf  .outlet   ---> dac    .inlet
    tSfVol  .outlet   ---> gainSf .inlet2

    route.outlets(0) ---> msTime.inlet
    msTime.outlet    ---> mrTime.inlet

    route.outlets(2) ---> msDur.inlet
    msDur.outlet     ---> mrDur.inlet

    route.outlets(3) ---> bEnded.inlet
    route.outlets(4) ---> printSf.inlet
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
