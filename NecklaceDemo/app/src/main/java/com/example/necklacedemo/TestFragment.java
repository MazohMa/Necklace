package com.example.necklacedemo;

import android.app.Fragment;
import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import necklace.BeadsId;
import necklace.GiftBead;
import necklace.InteractiveExpandContext;
import necklace.MenuBead;
import necklace.Necklace;
import necklace.NecklaceView;
import necklace.NecklaceViewModel;
import necklace.scene.GiftVisibleInterceptor;
import necklace.scene.MenuVisibleInterceptor;
import necklace.viewmodelsupport.ViewModelDelegate;
import necklace.viewmodelsupport.ViewModelFactory;

public class TestFragment extends Fragment implements LifecycleOwner {
    private static final String LOG_TAG = "TestFragment";
    private NecklaceView mNecklaceView;
    private LifecycleRegistry mRegistry;
    private ViewModelFactoryImpl mViewModelFactoryImpl;

    public TestFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onCreateView");
        mRegistry = new LifecycleRegistry(this);
        mRegistry.markState(Lifecycle.State.CREATED);
        getLifecycle().addObserver(
                (GenericLifecycleObserver) (source, event) ->
                        Log.d(LOG_TAG, "onStateChanged: event =" + event));

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.test_fragment, container, false);
        mNecklaceView = (NecklaceView) view.findViewById(R.id.neckLaceView);
        mNecklaceView.onListener(this);
        mViewModelFactoryImpl = new ViewModelFactoryImpl();
        Necklace.of(this, mViewModelFactoryImpl)
                .register(new GiftBead.Factory(getActivity(), new GiftContext(), new GiftVisibleInterceptor()),
                        BeadsId.GIFT_ICON)
                .register(new MenuBead.Factory(getActivity(), new MenuContext(), new MenuVisibleInterceptor()),
                        BeadsId.MENU_ICON);
        return view;
    }

    class GiftContext implements InteractiveExpandContext {

        @Override
        public boolean checkActivityValid() {
            return false;
        }

        @Override
        public boolean isNetworkAvailable() {
            return false;
        }

        @Override
        public boolean checkNetToast() {
            return false;
        }

        @Override
        public void toast(int id) {

        }

        @Override
        public void toast(@org.jetbrains.annotations.Nullable String msg) {

        }

        @NotNull
        @Override
        public FragmentManager getChildFragmentManager() {
            return null;
        }

        @org.jetbrains.annotations.Nullable
        @Override
        public FragmentManager getSupportFragmentManager() {
            return null;
        }

        @Override
        public void showWebDialog(@org.jetbrains.annotations.Nullable String webUrl, boolean isContainerAutoPopup) {

        }

        @Override
        public void showWebDialog(@org.jetbrains.annotations.Nullable String url, int w, int h) {

        }

        @Override
        public void hideWebDialog() {

        }
    }

    class MenuContext implements InteractiveExpandContext {

        @Override
        public boolean checkActivityValid() {
            return false;
        }

        @Override
        public boolean isNetworkAvailable() {
            return false;
        }

        @Override
        public boolean checkNetToast() {
            return false;
        }

        @Override
        public void toast(int id) {

        }

        @Override
        public void toast(@org.jetbrains.annotations.Nullable String msg) {

        }

        @NotNull
        @Override
        public FragmentManager getChildFragmentManager() {
            return null;
        }

        @org.jetbrains.annotations.Nullable
        @Override
        public FragmentManager getSupportFragmentManager() {
            return null;
        }

        @Override
        public void showWebDialog(@org.jetbrains.annotations.Nullable String webUrl, boolean isContainerAutoPopup) {

        }

        @Override
        public void showWebDialog(@org.jetbrains.annotations.Nullable String url, int w, int h) {

        }

        @Override
        public void hideWebDialog() {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mRegistry.markState(Lifecycle.State.RESUMED);
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
        mRegistry.markState(Lifecycle.State.DESTROYED);
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRegistry.markState(Lifecycle.State.STARTED);
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mRegistry;
    }

    class ViewModelFactoryImpl implements ViewModelFactory {

        @Override
        public NecklaceViewModel create() {
            NecklaceViewModel necklaceViewModel = ViewModelDelegate.getViewModel(TestFragment.this);
            Log.i("Necklace", "getViewModule: " + necklaceViewModel);
            return necklaceViewModel;
        }
    }
}
