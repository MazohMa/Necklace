package necklace.viewmodelsupport;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.arch.lifecycle.ViewModelStore;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class YYLoveHolderFragment extends Fragment implements ViewModelStoreOwner {
    private static final String LOG_TAG = "YYLoveHolderFragment";

    private static final HolderFragmentManager
            sHolderFragmentManager = new YYLoveHolderFragment.HolderFragmentManager();

    /**
     * @hide
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public static final String HOLDER_TAG =
            "android.lifecycle.state.YYLoveStateProviderHolderFragment";

    private ViewModelStore mViewModelStore = new ViewModelStore();

    public YYLoveHolderFragment() {
        setRetainInstance(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");
        sHolderFragmentManager.holderFragmentCreated(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
        YYLoveHolderFragment fragment = sHolderFragmentManager.mNotCommittedFragmentHolders.remove(
                getParentFragment());
        if (fragment != null) {
            Log.e(LOG_TAG, "Failed to save a ViewModel for " + fragment);
        }
        mViewModelStore.clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return mViewModelStore;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public static YYLoveHolderFragment holderFragmentFor(Activity activity) {
        return sHolderFragmentManager.holderFragmentFor(activity);
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public static YYLoveHolderFragment holderFragmentFor(Fragment fragment) {
        return sHolderFragmentManager.holderFragmentFor(fragment);
    }

    @SuppressWarnings("WeakerAccess")
    static class HolderFragmentManager {
        Map<Fragment, YYLoveHolderFragment> mNotCommittedFragmentHolders = new HashMap<>();
        Map<Activity, YYLoveHolderFragment> mNotCommittedActivityHolders = new HashMap<>();
        private Application.ActivityLifecycleCallbacks mActivityCallbacks =
                new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity, Bundle bundle) {
                        Log.i(LOG_TAG, "onActivityCreated" + activity);
                    }

                    @Override
                    public void onActivityStarted(Activity activity) {
                        Log.i(LOG_TAG, "onActivityStarted" + activity);
                    }

                    @Override
                    public void onActivityResumed(Activity activity) {
                        Log.i(LOG_TAG, "onActivityResumed" + activity);
                    }

                    @Override
                    public void onActivityPaused(Activity activity) {
                        Log.i(LOG_TAG, "onActivityPaused" + activity);
                    }

                    @Override
                    public void onActivityStopped(Activity activity) {
                        Log.i(LOG_TAG, "onActivityStopped" + activity);

                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                        Log.i(LOG_TAG, "onActivitySaveInstanceState" + activity);
                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {
                        Log.i(LOG_TAG, "onActivityDestroyed" + activity);
                        YYLoveHolderFragment fragment = mNotCommittedActivityHolders.remove(activity);
                        if (fragment != null) {
                            Log.e(LOG_TAG, "Failed to save a ViewModel for " + activity);
                        }
                    }
                };
        private android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks mParentDestroyedCallback =
                new android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentDestroyed(android.support.v4.app.FragmentManager fm,
                                                    android.support.v4.app.Fragment parentFragment) {
                        super.onFragmentDestroyed(fm, parentFragment);
                        YYLoveHolderFragment fragment = mNotCommittedFragmentHolders.remove(
                                parentFragment);
                        if (fragment != null) {
                            Log.e(LOG_TAG, "Failed to save a ViewModel for " + parentFragment);
                        }
                    }
                };
        private boolean mActivityCallbacksIsAdded = false;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        void holderFragmentCreated(Fragment holderFragment) {
            Fragment parentFragment = holderFragment.getParentFragment();
            if (parentFragment != null) {
                mNotCommittedFragmentHolders.remove(parentFragment);
            } else {
                mNotCommittedActivityHolders.remove(holderFragment.getActivity());
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        private static YYLoveHolderFragment findHolderFragment(FragmentManager manager) {
            Log.i(LOG_TAG, "findHolderFragment");
            if (manager.isDestroyed()) {
                throw new IllegalStateException("Can't access ViewModels from onDestroy");
            }

            Fragment fragmentByTag = manager.findFragmentByTag(HOLDER_TAG);
            if (fragmentByTag != null && !(fragmentByTag instanceof YYLoveHolderFragment)) {
                throw new IllegalStateException("Unexpected "
                        + "fragment instance was returned by HOLDER_TAG");
            }
            return (YYLoveHolderFragment) fragmentByTag;
        }

        private static YYLoveHolderFragment createHolderFragment(FragmentManager fragmentManager) {
            Log.i(LOG_TAG, "createHolderFragment");
            YYLoveHolderFragment holder = new YYLoveHolderFragment();
            fragmentManager.beginTransaction().add(holder, HOLDER_TAG).commitAllowingStateLoss();
            return holder;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        YYLoveHolderFragment holderFragmentFor(Activity activity) {
            Log.i(LOG_TAG, "holderFragmentFor");
            FragmentManager fm = activity.getFragmentManager();
            YYLoveHolderFragment holder = findHolderFragment(fm);
            if (holder != null) {
                return holder;
            }
            holder = mNotCommittedActivityHolders.get(activity);
            if (holder != null) {
                return holder;
            }

            if (!mActivityCallbacksIsAdded) {
                mActivityCallbacksIsAdded = true;
                activity.getApplication().registerActivityLifecycleCallbacks(mActivityCallbacks);
            }
            holder = createHolderFragment(fm);
            mNotCommittedActivityHolders.put(activity, holder);
            return holder;
        }

        YYLoveHolderFragment holderFragmentFor(Fragment parentFragment) {
            FragmentManager fm = parentFragment.getChildFragmentManager();
            YYLoveHolderFragment holder = findHolderFragment(fm);
            if (holder != null) {
                return holder;
            }
            holder = mNotCommittedFragmentHolders.get(parentFragment);
            if (holder != null) {
                return holder;
            }
            holder = createHolderFragment(fm);
            mNotCommittedFragmentHolders.put(parentFragment, holder);
            return holder;
        }

    }
}
