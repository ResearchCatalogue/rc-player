package rc

import rc.view.PatcherView

object Patcher {
  sealed trait Update { def patcher: Patcher }
  case class Added  (elem: Elem)
  case class Removed(elem: Elem)
}
trait Patcher extends Model[Patcher.Update] {
  def dispose(): Unit
  def view(): PatcherView
}
