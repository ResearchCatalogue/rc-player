/*
 *  package.scala
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

import org.scalajs.dom.ModifierKeyEvent
import rc.objects.Bang
import rc.view.impl.{CordViewImpl, BangViewImpl}

package object view {
  def BangView(parentView: PatcherView, bang: Bang): NodeView = new BangViewImpl(parentView, bang)
  def CordView(parentView: PatcherView, cord: Cord): View     = new CordViewImpl(parentView, cord)

  def isMenu(e: ModifierKeyEvent): Boolean = if (isMac) e.metaKey else e.ctrlKey
}
