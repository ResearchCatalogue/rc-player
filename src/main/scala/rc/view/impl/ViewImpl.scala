/*
 *  ViewImpl.scala
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
package impl

trait ViewImpl extends View {
  protected def init(): Unit = {
    // handle selection
    peer.mousePressed { e =>
      if (e.button == 0 && !e.defaultPrevented && parentView.editing && !isMenu(e)) {
        val sel = parentView.selection
        if (!sel.contains(this)) {
          if (!e.shiftKey) sel.clear()
          sel.add(this)
        }
        // new DragBox(this, e)
        // e.stopPropagation()
        e.preventDefault()
      }
    }
  }
}
