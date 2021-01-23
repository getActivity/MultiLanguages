package com.hjq.language;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sSystemLanguage = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else  {
            sSystemLanguage = Resources.getSystem().getConfiguration().locale;
        }
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
    static void register(Context context) {
        context.registerComponentCallbacks(new LanguagesObserver());
    }

    /**
     * 手机的语种发生了变化
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Locale newLocale = LanguagesTools.getLocale(newConfig);

        Locale oldLocale = sSystemLanguage;
        if (newLocale.equals(oldLocale)) {
            return;
        }
        sSystemLanguage = newLocale;
        OnLanguageListener listener = MultiLanguages.getOnLanguagesListener();
        if (listener != null) {
            listener.onSystemLocaleChange(oldLocale, newLocale);
        }
    }

    @Override
    public void onLowMemory() {}
}