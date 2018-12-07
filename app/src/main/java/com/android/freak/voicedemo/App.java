package com.android.freak.voicedemo;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * Created by Administrator on 2018/12/7.
 */

public class App extends Application {
    private static Context mContext;
    private static App mApp;
    public static Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
        FormatStrategy mFormatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // （可选）是否显示线程信息。默认值true
//                .methodCount(5)         // （可选）要显示的方法行数。默认值2
//                .methodOffset(7)        // （可选）隐藏内部方法调用到偏移量。默认值5
//                .logStrategy() // （可选）更改要打印的日志策略。默认LogCat
                .tag("VoiceDemo")   // （可选）每个日志的全局标记。默认PRETTY_LOGGER .build
                .build();
        //log日志打印框架Logger
        Logger.addLogAdapter(new AndroidLogAdapter(mFormatStrategy));
    }

    public static App getInstance() {
        if (mApp == null) {
            synchronized (App.class) {
                if (mApp == null) {
                    mApp = new App();
                }
            }
        }
        return mApp;
    }
}
