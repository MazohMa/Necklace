package necklace

import android.app.Fragment
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import io.reactivex.disposables.CompositeDisposable
import necklace.viewmodelsupport.ViewModelDelegate

/**
 * Created by wangfeihang on 2019/1/2.
 */
class NecklaceView : LinearLayout {

    companion object {
        const val TAG = "NecklaceView"
    }

    private var mViewModel: NecklaceViewModel? = null
    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?)
        : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
        : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        Log.i(TAG, "init")
        this.orientation = HORIZONTAL
        this.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    fun onOrientationChanged(isLandscape: Boolean) {
        orientation = if (isLandscape) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL
        mViewModel?.onOrientationChanged(isLandscape)
    }

    public fun onListener(fragment: Fragment) {
        Log.i(TAG, "onListener")
        mViewModel = ViewModelDelegate.getViewModel(fragment).apply {
            this.observableCurNecklace
                .observe(fragment as LifecycleOwner, Observer { curNecklace ->
                    Log.i(TAG, "add view list:$curNecklace")
                    removeAllViews()
                    curNecklace?.forEach {
                        val view = it.realBead.getView()
                        if (view == null) {
                            it.expectedVisible.value = false
                        } else {
                            view.parent?.let { parent ->
                                if (parent is ViewGroup) {
                                    parent.removeView(view)
                                }
                            }
                            addView(view)
                            view.apply {
                                fun View.setMargin(margin: Int) {
                                    this.layoutParams = (this.layoutParams as LinearLayout.LayoutParams)
                                        .also {
                                            it.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                                            it.setMargins(margin, margin, margin, margin)
                                        }
                                }
                                this.setMargin(DimensUtils.dip2pixel(context, 4f))
                                this.visibility = View.VISIBLE
                            }
                            if (it.isChangeToVisible) {
                                it.realBead.onVisibleChange(true, curNecklace.indexOf(it))
                            }
                            it.isChangeToVisible = false
                        }
                        it.realBead.onLayoutChanged()
                    }
                })
        }
        Log.i(TAG, "getViewModule =$mViewModel")
    }
}