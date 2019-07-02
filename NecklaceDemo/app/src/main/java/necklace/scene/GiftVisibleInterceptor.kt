package necklace.scene

/**
 * Created by maxiaokun on 2019/5/29.
 */
class GiftVisibleInterceptor : VisibleInterceptor() {

    companion object {
        private const val TAG = "GiftVisibleInterceptor"
    }

    override fun intercept(): Boolean {
        return false
    }

    override fun handle() {
        super.handle()
    }
}