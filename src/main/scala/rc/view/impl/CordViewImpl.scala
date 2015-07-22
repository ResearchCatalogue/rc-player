package rc
package view
package impl

import org.scalajs.dom

class CordViewImpl(val parentView: PatcherView, val elem: Cord) extends View {
  val peer: dom.svg.Line = {
    import scalatags.JsDom.all.{height => _, width => _, _}
    import scalatags.JsDom.svgTags._
    val lineElem    = line(cls := s"pat-cord pat-${tpe.name}-cord").render
    val source      = elem.source
    val sink        = elem.sink
    val sourceView  = parentView.getView(source.node)
    val sinkView    = parentView.getView(sink  .node)
    sourceView.foreach(setSourceLoc)
    sinkView  .foreach(setSinkLoc  )
    lineElem
  }

  private def setSourceLoc(view: View): Unit = view match {
    case n: NodeView =>
      val loc     = n.portLocation(elem.source)
      val bounds  = n.peer.getBoundingClientRect()
      peer.x1.baseVal.value = bounds.left + loc.x
      peer.y1.baseVal.value = bounds.top  + loc.x
    case _ => Console.err.println(s"Cord $elem is not connected to a node?")
  }

  private def setSinkLoc(view: View): Unit = view match {
    case n: NodeView =>
      val loc     = n.portLocation(elem.sink)
      val bounds  = n.peer.getBoundingClientRect()
      peer.x2.baseVal.value = bounds.left + loc.x
      peer.y2.baseVal.value = bounds.top  + loc.x
    case _ => Console.err.println(s"Cord $elem is not connected to a node?")
  }

  def dispose(): Unit = ()
}
