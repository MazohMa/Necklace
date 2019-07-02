package necklace
import necklace.scene.VisibleInterceptor

/**
 * Created by 张宇 on 2019/1/2.
 * E-mail: zhangyu4@yy.com
 * YY: 909017428
 */
interface NecklaceContext {

    /**
     * 获取指定[beadsId]的业务按钮是否可见。
     * 如果有多个[beadsId]相同的按钮，只要存在一个可见，该方法返回true。
     *
     * @param beadsId 指定业务，默认为空表示当前业务。
     */
    @Deprecated(message = "bead list 的管理是在io线程做的，取的值不是实时的")
    fun isVisible(beadsId: BeadsId? = null): Boolean

    /**
     * 申请自己需要显示。
     * 但最终能否显示出来，需要由Necklace根据优先级来决定。
     */
    fun requestShowSelf()

    /**
     * 申请自己需要隐藏。
     */
    fun requestHideSelf()

    /**
     * 这是可见性拦截器，一般由外部业务方传进来
     */
    fun setVisibleInterceptor(visibleInterceptor: VisibleInterceptor)

    fun clearVisibleInterceptor()
}