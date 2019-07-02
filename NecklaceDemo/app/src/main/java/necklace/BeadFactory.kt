package necklace

import android.arch.lifecycle.Lifecycle

/**
 * Created by 张宇 on 2019/1/3.
 * E-mail: zhangyu4@yy.com
 * YY: 909017428
 */
interface BeadFactory {

    /**
     * 创建[Bead]实例。
     *
     * @param necklace 底部按钮的上下文，用于可见性的控制等
     * @param lifecycle 所在[android.app.Activity]的生命周期
     * @param beadsConfig 当前业务的后台配置
     */
    fun create(
        necklace: NecklaceContext,
        lifecycle: Lifecycle,
        beadsConfig: BeadsConfig
    ): Bead
}