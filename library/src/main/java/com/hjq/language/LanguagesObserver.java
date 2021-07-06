package com.hjq.language;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2019/05/06
 *    desc   : 语种变化监听
 */
final class LanguagesObserver implements ComponentCallbacks {

    /** 系统语种 */
    private static volatile Locale sSystemLanguage;

    static {
        // 获取当前系统的语种
        sSystemLanguage = LanguagesUtils.getLocale(Resources.getSystem().getConfiguration());
    }

    /**
     * 获取系统的语种
     */
    static Locale getSystemLanguage() {
        return sSystemLanguage;
    }

    /**
     * 注册系统语种变化监听
     */
    static void register(Application application) {
        application.registerComponentCallbacks(new LanguagesObserver());
    }

    /**
     * 手机的配置发生了变化
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Locale newLocale = LanguagesUtils.getLocale(newConfig);

        Locale oldLocale = sSystemLanguage;

        // 更新 Application 的配置，否则会出现横竖屏切换之后 Application 的 orientation 没有随之变化的问题
        LanguagesUtils.updateConfigurationChanged(MultiLanguages.getApplication(), newConfig);

        if (newLocale.equals(oldLocale)) {
            return;
        }
        sSystemLanguage = newLocale;

        // 如果当前的语种是跟随系统变化的，那么就需要重置一下当前 App 的语种
        if (LanguagesConfig.isSystemLanguage(MultiLanguages.getApplication())) {
            LanguagesConfig.clearLanguage(MultiLanguages.getApplication());
        }

        OnLanguageListener listener = MultiLanguages.getOnLanguagesListener();
        if (listener != null) {
            listener.onSystemLocaleChange(oldLocale, newLocale);
        }
    }

    @Override
    public void onLowMemory() {}
}