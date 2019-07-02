package necklace

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.example.necklacedemo.R
import necklace.scene.VisibleInterceptor

/**
 * copy from [GiftModule]
 */
class GiftBead(
    private val expandContext: InteractiveExpandContext,
    private val context: Activity,
    private val lifecycle: Lifecycle,
    necklace: NecklaceContext,
    private val visibleInterceptor: VisibleInterceptor?
) : Bead(necklace) {
    override fun onCreateView(): View? {
        if (context == null) {
            return null
        }
        return LayoutInflater.from(context)
            .inflate(R.layout.icon_interactive_expand_gift, FrameLayout(context).apply {
                layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            })
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        view.setOnClickListener {
            Toast.makeText(context, "点击····", Toast.LENGTH_SHORT).show()
        }
    }

    class Factory(
        private val context: Activity,
        private val expandContext: InteractiveExpandContext,
        private val visibleInterceptor: VisibleInterceptor?
    ) :
        BeadFactory {

        override fun create(necklace: NecklaceContext, lifecycle: Lifecycle, beadsConfig: BeadsConfig) =
            GiftBead(expandContext, context, lifecycle, necklace, visibleInterceptor)
    }
}