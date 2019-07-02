package necklace

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build

/**
 * Created by wangfeihang on 2018/11/27.
 */
fun Activity?.isLandScape(): Boolean {
    return this?.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
        || this?.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        || this?.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

fun Activity?.isPortrait(): Boolean {
    return this?.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        || this?.requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
}

fun Activity?.getConfigurationsOrientation(): Int {
    return if (this == null || this.isLandScape()) {
        Configuration.ORIENTATION_LANDSCAPE
    } else {
        Configuration.ORIENTATION_PORTRAIT
    }
}

fun Activity.isValid(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        !this.isDestroyed && !this.isFinishing
    } else {
        true
    }
}