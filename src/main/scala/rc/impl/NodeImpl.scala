/*
 *  NodeImpl.scala
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
package impl

import scala.collection.mutable

trait NodeImpl {
  node: Node =>

  object state extends State with ModelImpl[State.Update] {
    private val map = mutable.Map.empty[String, Any]

    def put(key: String, value: Any): Unit =
      map.put(key, value).fold {
        dispatch(State.Added(node, key = key, value = value))
      } { oldValue =>
        if (value != oldValue) dispatch(State.Changed(node, key = key, before = oldValue, now = value))
      }

    def get(key: String): Option[Any] = map.get(key)

    def remove(key: String): Unit =
      map.remove(key).foreach { oldValue =>
        dispatch(State.Removed(node, key = key, value = oldValue))
      }
  }
}