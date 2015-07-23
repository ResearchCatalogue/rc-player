/*
 *  Message.scala
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

package rc.objects

import rc.{M, Patcher}
import rc.impl.{ModelImpl, NodeImpl, NodeImplOps}
import rc.view.{MessageNodeView, PatcherView, View}

import scala.scalajs.js

class Message(val parent: Patcher, var args: List[Any])
  extends NodeImpl
  with rc.Message
  with ModelImpl[String] {

  private val _vars = js.Array[Any]()
  private var _message: M = null

  private def argsUpdated(): Unit = {
    _message  = null // invalidate
    var idx = 0; while (idx < 9) { _vars(idx) = 0; idx += 1 }
    dispatch(contents)
  }

  private def updateMessage(): Unit = {
    val b = List.newBuilder[Any]
    args.foreach {
      case arg: String if arg.length == 2 && arg.charAt(0) == '$' =>
        val c = arg.charAt(1)
        if (c >= '0' && c <= '9') _vars(c - '0') else arg

      case arg => b += arg
    }
    _message = M(b.result(): _*)
  }

  def message: M = {
    if (_message == null) updateMessage()
    _message
  }

  def contents: String = args.mkString(" ")

  val outlet = this.messageOutlet

  val inlet = this.messageInlet {
    case M.Bang =>
      outlet(message)
    case M("set", rest @ _*) =>
      args = rest.toList
      argsUpdated()
    case M("append", rest @ _*) =>
      args ++= rest
      argsUpdated()
    case M("prepend", rest @ _*) =>
      args :::= rest.toList
      argsUpdated()
    case other =>
      val it  = other.atoms.iterator
      var idx = 0
      while (it.hasNext && idx < 9) {
        _vars(idx) = it.next()
        idx += 1
      }
      outlet(message)
  }

  def view(parentView: PatcherView): View = MessageNodeView(parentView, this)
}
