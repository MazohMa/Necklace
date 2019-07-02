package necklace

import android.support.annotation.StringRes
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

/**
 * Created by 张宇 on 2018/12/15.
 * E-mail: zhangyu4@yy.com
 * YY: 909017428
 */
interface InteractiveExpandContext {

    fun checkActivityValid(): Boolean

    fun isNetworkAvailable(): Boolean

    fun checkNetToast(): Boolean

    fun toast(@StringRes id: Int)

    fun toast(msg: String?)

    fun getChildFragmentManager(): FragmentManager

    fun getSupportFragmentManager(): FragmentManager?

    fun showWebDialog(webUrl: String?, isContainerAutoPopup: Boolean)

    fun showWebDialog(url: String?, w: Int, h: Int)

    fun hideWebDialog()
}