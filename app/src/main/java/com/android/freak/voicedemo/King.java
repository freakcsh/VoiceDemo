package com.android.freak.voicedemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jiangkang on 2017/9/6.
 */

public final class King {

    private static Application sContext;

    private static List<Activity> sActivityList = new LinkedList<>();

    private static WeakReference<Activity> sTopActivityWeakRef;

    private static Application.ActivityLifecycleCallbacks sCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            sActivityList.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            setTopActivity(activity);
//            activity.sendBroadcast(new Intent("com.jiangkang.ktools.ActivityChange"));
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            sActivityList.remove(activity);
        }
    };

    public static void init(@NonNull final Application context){
        sContext = context;
        sContext.registerActivityLifecycleCallbacks(sCallbacks);
    }

    public static Context getApplicationContext(){
        if (sContext == null) throw new NullPointerException("context can not be null");
        return sContext;
    }


    private static void setTopActivity(Activity activity){
            sTopActivityWeakRef = new WeakReference<>(activity);
    }


    public static WeakReference<Activity> getTopActivityWeakRef() {
        return sTopActivityWeakRef;
    }


    public static List<Activity> getsActivityList() {
        return sActivityList;
    }
}
