/*
 *  PatcherSelectionImpl.scala
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
package view
package impl

import rc.impl.ModelImpl

import scala.collection.mutable

class PatcherSelectionImpl(patcher: PatcherView) extends PatcherSelection with ModelImpl[PatcherSelection.Update] {
  import PatcherSelection.{Added, Removed}

  private val coll = mutable.Set.empty[View]

  def contains(elem: View): Boolean = coll.contains(elem)

  def clear(): Unit = if (coll.nonEmpty) {
    val removed = coll.toList
    coll.clear()
    dispatch(Removed(patcher, removed: _*))
  }

  def add(elems: View*): Unit = {
    val added = elems.filter(coll.add)
    if (added.nonEmpty) dispatch(Added(patcher, added: _*))
  }

  def remove(elems: View*): Unit = {
    val removed = elems.filter(coll.remove)
    if (removed.nonEmpty) dispatch(Removed(patcher, removed: _*))
  }

  def size    : Int     = coll.size
  def isEmpty : Boolean = coll.isEmpty
  def nonEmpty: Boolean = coll.nonEmpty

  def get: Set[View] = coll.toSet
}