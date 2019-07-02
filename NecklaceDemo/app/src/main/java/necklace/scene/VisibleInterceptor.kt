package necklace.scene

/**
 * Created by wangfeihang on 2019/1/10.
 * 抽象可见性拦截器，实现类让调用方去实现，sdk内部不对具体的业务拦截做处理 by xiaokun
 */
abstract class VisibleInterceptor {

    companion object {
        private const val TAG = "VisibleInterceptor"
    }

    /**
     * 拦截处理监听
     */
    private var onStateChangedListener: () -> Unit? = {}

    /**
     * 这个接口的作用是可见性状态监听
     */
    fun setOnStateChange(onStateChange: () -> Unit) {
        onStateChangedListener = onStateChange
    }

    /**
     * 拦截处理
     */
    open fun handle() {
        onStateChangedListener()
    }

    /**
     * 是否拦截
     */
    abstract fun intercept(): Boolean
}