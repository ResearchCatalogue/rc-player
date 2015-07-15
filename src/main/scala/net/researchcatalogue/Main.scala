/*
 *  Main.scala
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

package net.researchcatalogue

import org.scalajs.jquery.{jQuery => $}

import scala.scalajs.js

object Main extends js.JSApp {
  def main(): Unit = $(documentLoaded _)

  private def documentLoaded(): Unit = {
    val but = new PlayButton
    $("#player").append(but.render)

    implicit val as = new AudioSystem
    val noise = new WhiteNoise
    val dac   = new DAC
    noise.out_~ add dac.in_~
  }
}
