/*
 *  ObjNodeImpl.scala
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

package rc.impl

import rc.ObjNode
import rc.view.View

trait ObjNodeImpl extends NodeImpl with ObjNode {
  /** The default implementation calls `View(this)` */
  def view(): View = View(this)
}