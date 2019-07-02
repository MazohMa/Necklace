package necklace.scene

/**
 * Created by maxiaokun on 2019/5/29.
 */
class MenuVisibleInterceptor : VisibleInterceptor() {

    companion object {
        private const val TAG = "MenuVisibleInterceptor"
    }

    override fun intercept(): Boolean {
        return false
    }

    override fun handle() {
        super.handle()
    }
}