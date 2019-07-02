package com.example.necklacedemo;

import android.app.Activity;
import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity implements LifecycleOwner {
    private static final String TAG = "MainActivity";
    private LifecycleRegistry mRegistry;
    public static final String HOLDER_TAG =
            "android.lifecycle.state.TestFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRegistry = new LifecycleRegistry(this);
        mRegistry.markState(Lifecycle.State.CREATED);
        getLifecycle().addObserver(
                (GenericLifecycleObserver) (source, event) ->
                        Log.d("MainActivity", "onStateChanged: event =" + event));
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mRegistry;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRegistry.markState(Lifecycle.State.STARTED);
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRegistry.markState(Lifecycle.State.RESUMED);
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRegistry.markState(Lifecycle.State.DESTROYED);
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }
}
