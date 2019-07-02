package necklace

import android.os.Looper
import android.support.annotation.CallSuper
import android.support.annotation.MainThread
import android.view.View

/**
 * Created by 张宇 on 2019/1/2.
 * E-mail: zhangyu4@yy.com
 * YY: 909017428
 *
 * 直播间底部按钮
 */
abstract class Bead(val necklace: NecklaceContext) {

    private var view: View? = null

    /**
     * 底部按钮被创建时被回调。
     * 一般在调用register方法后，底部按钮会通过[BeadFactory]被创建。
     *
     * 注意该方法与[android.app.Activity.onCreate]的时机不一定相同。
     */
    @CallSuper
    open fun onCreate() {
    }

    /**
     * 当底部按钮从不可见变为可见，而且从未创建过View的情况下被回调。
     * 由于底部按钮默认为不可见状态，所以[NecklaceContext.requestShowSelf]
     * 方法是触发创建View的入口。
     *
     * 如果当前方法返回null，那么该底部按钮将不会添加到底部布局中，直到下一次
     * [NecklaceContext.requestShowSelf]方法
     * 调用时，该方法再次被回调。
     */
    abstract fun onCreateView(): View?

    /**
     * 当且仅当[onCreateView]返回非空时，该方法会被回调。
     */
    open fun onViewCreated(view: View) {}

    /**
     * 底部按钮被销毁时回调。
     * 注意该方法与[android.app.Activity.onDestroy]的时机不一定相同。
     */
    @CallSuper
    open fun onDestroy() {
        //小米反馈内存泄漏
        necklace.clearVisibleInterceptor()
    }

    /**
     * 当前[android.app.Activity]的屏幕方向发生变化时回调。
     */
    open fun onOrientationChange(isLandscape: Boolean) {
    }

    /**
     * 当前按钮可见性发生变化时回调。
     * 按钮默认为不可见，直到调用[NecklaceContext.requestShowSelf]方法使其从不可见变为可见时，
     * 该方法回调[visible]为true，且[position]为显示的位置。
     *
     * 当调用[NecklaceContext.requestHideSelf]方法使其从可见变为不可见，或者有更高优先级的按钮取代，
     * 该方法回调[visible]为false，且[position]为隐藏之前的位置。
     */
    open fun onVisibleChange(visible: Boolean, position: Int) {}

    open fun onLayoutChanged() {}

    /**
     * 获取当前底部按钮的View
     */
    @MainThread
    fun getView(): View? {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw IllegalThreadStateException("getView() should be called in main thread!")
        }
        return view ?: onCreateView().also {
            view = it
            it?.post { onViewCreated(it) }
        }
    }
}