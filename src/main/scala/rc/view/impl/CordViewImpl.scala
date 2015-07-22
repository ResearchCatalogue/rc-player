package rc
package view
package impl

import org.scalajs.dom

class CordViewImpl(val parentView: PatcherView, val elem: Cord) extends View {
  val peer: dom.svg.Line = {
    import scalatags.JsDom.all.{height => _, width => _, tpe => _, _}
    import scalatags.JsDom.svgTags._
    line(cls := s"pat-cord pat-${elem.tpe.name}-cord").render
  }

  init()

  private def init(): Unit = {
    val source      = elem.source
    val sink        = elem.sink
    val sourceView  = parentView.getView(source.node)
    val sinkView    = parentView.getView(sink  .node)
    sourceView.foreach(setSourceLoc)
    sinkView  .foreach(setSinkLoc  )
  }

  private def setSourceLoc(view: View): Unit = view match {
    case n: NodeView =>
      val loc     = n.portLocation(elem.source)
      val bounds  = n.peer.getBoundingClientRect()
      val pBounds = n.parentView.container.getBoundingClientRect()
      // println(s"SOURCE BOUNDS (${bounds.left}, ${bounds.top}, ${bounds.width}, ${bounds.height})")
      val x       = bounds.left - pBounds.left + loc.x + 0.5
      val y       = bounds.top  - pBounds.top  + loc.y + 0.5
      peer.x1.baseVal.value = x
      peer.y1.baseVal.value = y

      // println(s"source loc = ($x, $y)")

    case _ => Console.err.println(s"Cord $elem is not connected to a node?")
  }

  private def setSinkLoc(view: View): Unit = view match {
    case n: NodeView =>
      val loc     = n.portLocation(elem.sink)
      val bounds  = n.peer.getBoundingClientRect()
      val pBounds = n.parentView.container.getBoundingClientRect()
      val x       = bounds.left - pBounds.left + loc.x + 0.5
      val y       = bounds.top  - pBounds.top  + loc.y + 0.5
      peer.x2.baseVal.value = x
      peer.y2.baseVal.value = y

      // println(s"sink   loc = ($x, $y)")

    case _ => Console.err.println(s"Cord $elem is not connected to a node?")
  }

  def dispose(): Unit = ()
}
