package rc
package view
package impl

trait ViewImpl extends View {
  protected def init(): Unit = {
    // handle selection
    peer.mousePressed { e =>
      if (e.button == 0 && !e.defaultPrevented && !isMenu(e)) {
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
