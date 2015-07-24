/*
 *  View.scala
 *  (rc-player)
 *
 *  Copyright (c) 2015 Society of Artistic Research (SAR). All rights reserved.
 *  Written by Hanns Holger Rutz.
 *
 *	This software is published under a BSD 2-Clause License.
 *
 *
 *	For further information, please contact Hanns Holger Rutz at
 *	contact@sciss.de
 */

package rc
package view

import org.scalajs.dom

trait View extends Disposable {
  def parentView: PatcherView

  def elem: Elem

  def peer: dom.svg.Element
}
