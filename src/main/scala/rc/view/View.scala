/*
 *  View.scala
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
package view

import org.scalajs.dom
import rc.view.impl.ObjNodeViewImpl

object View {
  /** Standard view for object nodes. */
  def apply(obj: ObjNode): View = new ObjNodeViewImpl(obj)
}
trait View extends Disposable {
  def elem: Elem

  def peer: dom.svg.Element
}
