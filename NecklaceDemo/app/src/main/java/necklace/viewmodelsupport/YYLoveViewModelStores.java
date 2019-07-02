package necklace.viewmodelsupport;

import android.app.Activity;
import android.app.Fragment;
import android.arch.lifecycle.ViewModelStore;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.os.Build;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

public class YYLoveViewModelStores {
    private YYLoveViewModelStores() {
    }

    /**
     * Returns the {@link ViewModelStore} of the given activity.
     *
     * @param activity an activity whose {@code ViewModelStore} is requested
     * @return a {@code ViewModelStore}
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @NonNull
    @MainThread
    public static ViewModelStore of(@NonNull Activity activity) {
        return YYLoveHolderFragment.holderFragmentFor(activity).getViewModelStore();
    }

    /**
     * Returns the {@link ViewModelStore} of the given fragment.
     *
     * @param fragment a fragment whose {@code ViewModelStore} is requested
     * @return a {@code ViewModelStore}
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @NonNull
    @MainThread
    public static ViewModelStore of(@NonNull Fragment fragment) {
        return YYLoveHolderFragment.holderFragmentFor(fragment).getViewModelStore();
    }
}
