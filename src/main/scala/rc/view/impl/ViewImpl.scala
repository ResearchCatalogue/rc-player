package rc
package view
package impl

import org.scalajs.dom

trait ViewImpl extends View {
  protected def init(): Unit = {
    // handle selection
    peer.onmousedown = { e: dom.MouseEvent =>
      if (e.button == 0 && !e.defaultPrevented) {
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
