package necklace.viewmodelsupport

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import necklace.NecklaceViewModel

class ViewModelDelegate {

    companion object {
        @JvmStatic
        fun getViewModel(fragment: android.app.Fragment): NecklaceViewModel {
            return YYLoveViewModelProviders.of(fragment, null).get(NecklaceViewModel::class.java)
        }

        @JvmStatic
        fun getViewModel(activity: Activity): NecklaceViewModel {
            return YYLoveViewModelProviders.of(activity).get(NecklaceViewModel::class.java)
        }

        @JvmStatic
        fun getViewModel(fragment: Fragment): NecklaceViewModel {
            return ViewModelProviders.of(fragment, null).get(NecklaceViewModel::class.java)
        }
    }
}