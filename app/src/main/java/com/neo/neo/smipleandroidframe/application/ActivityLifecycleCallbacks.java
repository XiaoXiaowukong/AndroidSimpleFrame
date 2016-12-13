package com.neo.neo.smipleandroidframe.application;

import android.app.Activity;
import android.os.Bundle;

/**
 * 监测Activity生命周期的回调接口
 *
 * @author neo
 * @version 1.0
 */
public interface ActivityLifecycleCallbacks {
    /**
     * 监测activity的onStop方法的调用
     *
     * @param activity
     */
    public void onActivityStopped(Activity activity);

    /**
     * 监测activity的onStart方法的调用
     *
     * @param activity
     */
    public void onActivityStarted(Activity activity);

    /**
     * 监测activity的onSaveInstanceState方法的调用
     *
     * @param activity
     * @param outState
     */
    public void onActivitySaveInstanceState(Activity activity, Bundle outState);

    /**
     * 监测activity的onResume方法的调用
     *
     * @param activity
     */
    public void onActivityResumed(Activity activity);

    /**
     * 监测activity的onPause方法的调用
     *
     * @param activity
     */
    public void onActivityPaused(Activity activity);

    /**
     * 监测activity的onDestroy方法的调用
     *
     * @param activity
     */
    public void onActivityDestroyed(Activity activity);

    /**
     * 监测activity的onCreate方法的调用
     *
     * @param activity
     * @param savedInstanceState
     */
    public void onActivityCreated(Activity activity, Bundle savedInstanceState);

}
