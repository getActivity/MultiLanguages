package com.hjq.language;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import java.util.Locale;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2019/05/06
 *    desc   : 语种变化监听
 */
final class LanguagesObserver implements ComponentCallbacks, Runnable {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    /** 系统语种 */
    private static volatile Locale sSystemLanguage;

    /**
     * 注册系统语种变化监听
     */
    static void register(Application application) {
        sSystemLanguage = LanguagesUtils.getSystemLocale(application);
        LanguagesObserver languagesObserver = new LanguagesObserver();
        application.registerComponentCallbacks(languagesObserver);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 需要注意的是：Android 13 上面改变了系统的语种也不会回调 onConfigurationChanged 方法
            // 所以就需要借助一些特殊手段来获取，通过查阅官方文档，发现没有什么好的办法监听到，只能通过轮询的方式
            languagesObserver.startLoopRefresh();
        }
    }

    /**
     * 开启轮询刷新
     */
    public void startLoopRefresh() {
        HANDLER.postDelayed(this, 1000);
    }

    @Override
    public void run() {
        Locale latestSystemLocale = MultiLanguages.getSystemLanguage(MultiLanguages.getApplication());
        if (!MultiLanguages.equalsCountry(latestSystemLocale, sSystemLanguage)) {
            notifySystemLocaleChange(sSystemLanguage, latestSystemLocale);
        }
        startLoopRefresh();
    }

    /**
     * 手机的配置发生了变化
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig == null) {
            return;
        }
        Locale newLocale = LanguagesUtils.getLocale(newConfig);

        Locale oldLocale = sSystemLanguage;

        // 更新 Application 的配置，否则会出现横竖屏切换之后 Application 的 orientation 没有随之变化的问题
        LanguagesUtils.updateConfigurationChanged(MultiLanguages.getApplication(), newConfig);

        if (newLocale.equals(oldLocale)) {
            return;
        }
        notifySystemLocaleChange(oldLocale, newLocale);
    }

    /**
     * 通知系统语种发生变化
     */
    public void notifySystemLocaleChange(Locale oldLocale, Locale newLocale) {
        sSystemLanguage = newLocale;

        // 如果当前的语种是跟随系统变化的，那么就需要重置一下当前 App 的语种
        if (LanguagesConfig.isSystemLanguage(MultiLanguages.getApplication())) {
            LanguagesConfig.clearLanguageSetting(MultiLanguages.getApplication());
        }

        OnLanguageListener listener = MultiLanguages.getOnLanguagesListener();
        if (listener == null) {
            return;
        }
        listener.onSystemLocaleChange(oldLocale, newLocale);
    }

    @Override
    public void onLowMemory() {}
}