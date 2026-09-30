package com.hjq.language;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import androidx.annotation.NonNull;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2019/05/06
 *    desc   : 手机配置变化监听
 */
final class ConfigurationObserver implements ComponentCallbacks {

    private static volatile boolean sRegisterFlag = false;

    /**
     * 注册系统语种变化监听
     */
    static void register(@NonNull Application application) {
        if (sRegisterFlag) {
            return;
        }
        sRegisterFlag = true;

        application.registerComponentCallbacks(new ConfigurationObserver(application));
    }

    @NonNull
    private final Application mApplication;

    private ConfigurationObserver(@NonNull Application application) {
        mApplication = application;
    }

    /**
     * 手机的配置发生了变化
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        final Application application = mApplication;
        // 如果当前是跟随系统语种，就则不往下执行
        if (MultiLanguages.isSystemLanguage(application)) {
            return;
        }
        // 更新 Application 的配置，否则会出现横竖屏切换之后 Application 的 orientation 没有随之变化的问题
        LanguagesUtils.updateConfigurationChanged(application, newConfig, MultiLanguages.getAppLanguage(application));
    }

    @Override
    public void onLowMemory() {
        // default implementation ignored
    }
}