/*
 *  ModelImpl.scala
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

/** A straight forward implementation of a model. The trait implements all required methods and
  * provides a `dispatch` method to fire any updates.
  */
trait ModelImpl[U] extends Model[U] {
  protected type Listener = Model.Listener[U]
  private[this] var listeners = Vector.empty[Listener]

  /** Removes all listeners. This is useful when disposing the model, to remove any unnecessary references. */
  protected def releaseListeners(): Unit =
    listeners = Vector.empty

  /** Synchronously dispatches an update to all currently registered listeners. Non fatal exceptions are
    * caught on a per-listener basis without stopping the dispatch.
    */
  final protected def dispatch(update: U): Unit =
    listeners.foreach { pf => try {
        if (pf.isDefinedAt(update)) pf(update)
      } catch {
        case e: Exception /* scala.util.control.NonFatal(e) */ => e.printStackTrace()
      }
    }

  /** Subclasses can override this to issue particular actions when the first listener has been registered */
  protected def startListening(): Unit = ()
  /** Subclasses can override this to issue particular actions when the last listener has been unregistered */
  protected def stopListening (): Unit = ()

  def addListener(pf: Listener): pf.type = {
    val start = listeners.isEmpty
    listeners :+= pf
    if (start) startListening()
    pf
  }

  def removeListener(pf: Listener): Unit = {
    val idx = listeners.indexOf(pf)
    if (idx >=0 ) {
      listeners = listeners.patch(idx, Nil, 1)
      if (listeners.isEmpty) stopListening()
    }
  }
}