package com.hjq.language;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.util.Locale;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2023/11/24
 *    desc   : 语种变化广播
 */
final class LocaleChangeReceiver extends BroadcastReceiver {

    /** 系统语种 */
    private static volatile Locale sSystemLanguage;

    static void register(Application application) {
        sSystemLanguage = LanguagesUtils.getSystemLocale(application);
        IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
        application.registerReceiver(new LocaleChangeReceiver(), filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }

        String action = intent.getAction();

        if (action == null) {
            return;
        }

        if (!Intent.ACTION_LOCALE_CHANGED.equals(action)) {
            return;
        }

        if (sSystemLanguage == null) {
            return;
        }

        Locale latestSystemLocale = MultiLanguages.getSystemLanguage(MultiLanguages.getApplication());
        if (MultiLanguages.equalsCountry(latestSystemLocale, sSystemLanguage)) {
            return;
        }

        notifySystemLocaleChange(sSystemLanguage, latestSystemLocale);
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
}