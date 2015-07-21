package rc.sandbox

import rc.sandbox.Box

import scala.collection.immutable.{Seq => ISeq}
import scala.scalajs.js

object PatcherSelection {
  type Listener = PartialFunction[Update, Unit]
  case class Update(added: js.Array[Box], removed: js.Array[Box])
}
trait PatcherSelection {
  import PatcherSelection.Listener

  def contains(box: Box): Boolean

  def clear(): Unit

  def add   (boxes: Box*): Unit
  def remove(boxes: Box*): Unit

  def size: Int
  def isEmpty: Boolean
  def nonEmpty: Boolean

  def get: js.Array[Box]

  def addListener   (l: Listener): l.type
  def removeListener(l: Listener): Unit
}
