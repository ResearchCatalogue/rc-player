package rc.sandbox

import org.scalajs.jquery.{jQuery => $}

import scala.collection.immutable.{Seq => ISeq}
import scala.collection.mutable
import scala.scalajs.js

class PatcherSelectionImpl(patcher: PatcherOLD) extends PatcherSelection {
  import PatcherSelection.{Listener, Update}

  private val coll      = mutable.Set.empty[Box]
  private var listeners = List.empty[Listener]

  def contains(box: Box): Boolean = coll.contains(box)

  def clear(): Unit = if (coll.nonEmpty) {
    val removed = coll.to[js.Array]
    coll.clear()
    dispatch(Update(added = js.Array(), removed = removed))
  }

  def add(boxes: Box*): Unit = {
    val added = boxes.filter(coll.add)
    if (added.nonEmpty) dispatch(Update(added = added.to[js.Array], removed = js.Array()))
  }

  def remove(boxes: Box*): Unit = {
    val removed = boxes.filter(coll.remove)
    if (removed.nonEmpty) dispatch(Update(added = js.Array(),removed = removed.to[js.Array]))
  }

  def size    : Int     = coll.size
  def isEmpty : Boolean = coll.isEmpty
  def nonEmpty: Boolean = coll.nonEmpty

  def get: js.Array[Box] = coll.to[js.Array]

  def addListener(l: Listener): l.type = {
    listeners ::= l
    l
  }

  def removeListener(l: Listener): Unit = {
    val i = listeners.indexOf(l)
    if (i >= 0) {
      listeners = listeners.patch(i, Nil, 1)
    }
  }

  private def dispatch(u: Update): Unit = {
    u.removed.foreach { box =>
      box.render.classList.remove("selected")
    }
    u.added.foreach { box =>
      box.render.classList.add   ("selected")
    }
    listeners.foreach { l =>
      if (l.isDefinedAt(u)) try {
        l(u)
      } catch {
        case ex: Exception =>
          ex.printStackTrace()
      }
    }
  }
}