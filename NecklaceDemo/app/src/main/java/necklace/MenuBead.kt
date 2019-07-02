package necklace

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.example.necklacedemo.R
import kotlinx.android.synthetic.main.icon_interactive_expand_menu.view.*
import necklace.scene.VisibleInterceptor

/**
 * Created by 张宇 on 2019/1/7.
 * E-mail: zhangyu4@yy.com
 * YY: 909017428
 */
class MenuBead(
    val expandContext: InteractiveExpandContext,
    val context: Activity,
    necklace: NecklaceContext,
    val visibleInterceptor: VisibleInterceptor?
) : Bead
(necklace) {

    init {
        visibleInterceptor?.run {
            necklace.setVisibleInterceptor(this)
        }
    }

    private var redDot: View? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(): View? {
        val root = LayoutInflater.from(context)
            .inflate(R.layout.icon_interactive_expand_menu, FrameLayout(context).apply {
                this.layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            })
        redDot = root.btn_more_menu_red_dot
        return root
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        view.setOnClickListener {
            Toast.makeText(context, "点击····", Toast.LENGTH_SHORT).show()
        }
    }

    class Factory(
        private val context: Activity, private val expandContext: InteractiveExpandContext, private val
        visibleInterceptor: VisibleInterceptor?
    ) : BeadFactory {

        override fun create(necklace: NecklaceContext, lifecycle: Lifecycle, beadsConfig: BeadsConfig) =
            MenuBead(expandContext, context, necklace, visibleInterceptor)
    }

    companion object {
        const val TAG = "MenuBead"
    }
}