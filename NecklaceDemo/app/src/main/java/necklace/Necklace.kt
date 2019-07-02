package necklace

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import necklace.viewmodelsupport.ViewModelFactory

/**
 * Created by 张宇 on 2019/1/8.
 * E-mail: zhangyu4@yy.com
 * YY: 909017428
 */
object Necklace {

    @JvmStatic
    fun of(context: LifecycleOwner, viewModelFactory: ViewModelFactory?): ViewModelProxy? {
        if (context.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            return null
        }
        return ViewModelProxy(context, viewModelFactory)
    }

    class ViewModelProxy(private val context: LifecycleOwner, private val viewModelFactory: ViewModelFactory?) {
        fun register(beadFactory: BeadFactory, beadsId: BeadsId): ViewModelProxy {
            viewModelFactory?.create()?.register(beadFactory, beadsId, context.lifecycle)
            return this
        }

        fun unregister() {
            viewModelFactory?.create()?.destroyBead()
        }

        fun getBead(beadsId: BeadsId): Bead? {
            return viewModelFactory?.create()?.getBead(beadsId)
        }
    }
}