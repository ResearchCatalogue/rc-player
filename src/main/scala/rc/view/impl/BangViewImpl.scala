package rc
package view
package impl

import org.scalajs.dom.svg.Element
import rc.objects.Bang

import scalatags.JsDom.all._
import scalatags.JsDom.svgTags

class BangViewImpl(bang: Bang) extends View {
  val elem: Element = {
    import svgTags._
    rect(cls := "pat-node")(circle(cls := "pat-bang")).render
  }
}
