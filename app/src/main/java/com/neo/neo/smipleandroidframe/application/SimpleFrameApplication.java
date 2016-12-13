package com.neo.neo.smipleandroidframe.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;


import com.neo.neo.smipleandroidframe.utils.SharePreferenceUtils;

import java.util.Stack;


/**
 * Created by neo on 16/3/8.
 */
public class SimpleFrameApplication extends Application implements ActivityLifecycleCallbacks {
    public static SharePreferenceUtils sharePreferenceUtils;
    private static Stack<Activity> activityStack;
    private static SimpleFrameApplication singleton;

    @Override
    public void onCreate() {
        super.onCreate();
        getSharePreferenceInstance(this);
        singleton = this;
    }

    public static synchronized SharePreferenceUtils getSharePreferenceInstance(Context context) {
        if (sharePreferenceUtils == null) {
            sharePreferenceUtils = new SharePreferenceUtils(context);
        }
        return sharePreferenceUtils;
    }

    public static SimpleFrameApplication getInstance() {
        return singleton;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * add Activity 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
        for (int i = 0; i < activityStack.size(); i++) {
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 指定某一个不结束，其他的都结束
     *
     * @param cls
     */
    public void finishAllExcept(Class<?> cls) {
        int size = activityStack.size();
        for (int i = 0; i < size; i++) {
            if (null != activityStack.get(i)) {
                if (activityStack.get(i).getClass().equals(cls)) {
                } else {
                    activityStack.get(i).finish();
                }
            }
        }
    }
}
