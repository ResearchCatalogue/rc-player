package rc

import rc.objects.Bang

package object view {
  def BangView(bang: Bang): View = new impl.BangViewImpl(bang)
}
