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

import org.scalajs.dom
import org.scalajs.dom.ModifierKeyEvent
import rc.view.impl.{AudioCordViewImpl, ButtonViewImpl, MessageCordViewImpl, ToggleViewImpl}

package object view {
  def ButtonView(parentView: PatcherView, button: objects.Button): NodeView = new ButtonViewImpl(parentView, button)
  def ToggleView(parentView: PatcherView, toggle: objects.Toggle): NodeView = new ToggleViewImpl(parentView, toggle)

  def CordView(parentView: PatcherView, cord: Cord): View =
    if (cord.tpe == MessageType) new MessageCordViewImpl(parentView, cord)
    else                         new AudioCordViewImpl  (parentView, cord)

  def isMenu(e: ModifierKeyEvent): Boolean = if (isMac) e.metaKey else e.ctrlKey

  implicit class NodeViewOps(private val view: NodeView) extends AnyVal {
    /** Is a non-consumed left button click (in editing mode: while holding meta) */
    def isClickAction(e: dom.MouseEvent): Boolean =
      e.button == 0 && !e.defaultPrevented && (!view.parentView.editing || isMenu(e))
  }
}
