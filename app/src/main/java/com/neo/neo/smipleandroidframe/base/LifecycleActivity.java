package com.neo.neo.smipleandroidframe.base;

import android.app.Activity;
import android.os.Bundle;

import com.neo.neo.smipleandroidframe.application.ActivityLifecycleCallbacks;


/**
 * Created by kevin on 15/5/8.
 */
public class LifecycleActivity extends Activity {
    protected ActivityLifecycleCallbacks mApplication;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (ActivityLifecycleCallbacks) getApplication();
        mApplication.onActivityCreated(this, savedInstanceState);
    }

    protected void onResume() {

        mApplication.onActivityResumed(this);
        super.onResume();
    }

    @Override
    protected void onPause() {

        mApplication.onActivityPaused(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        mApplication.onActivityDestroyed(this);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        mApplication.onActivityStarted(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        mApplication.onActivityStopped(this);
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mApplication.onActivitySaveInstanceState(this, outState);
        super.onSaveInstanceState(outState);
    }
}

