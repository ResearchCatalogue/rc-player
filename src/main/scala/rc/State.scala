/*
 *  State.scala
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

object State {
  sealed trait Update { def node: Node; def key: String; def newValue: Option[Any] }
  case class Added  (node: Node, key: String, value: Any) extends Update {
    def newValue = Some(value)
  }
  case class Removed(node: Node, key: String, value: Any) extends Update {
    def newValue = None
  }
  case class Changed(node: Node, key: String, before: Any, now: Any) extends Update {
    def newValue = Some(now)
  }

  val Location = "loc"
}
trait State extends Model[State.Update] {
  def put(key: String, value: Any): Unit

  def remove(key: String): Unit

  // def putAll(pairs: (String, Any)*): Unit
  // def removeAll(keys: String*): Unit

  def get(key: String): Option[Any]
}