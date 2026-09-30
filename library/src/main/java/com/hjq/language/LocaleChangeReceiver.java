package com.hjq.language;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Locale;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2023/11/24
 *    desc   : 语种变化广播
 */
final class LocaleChangeReceiver extends BroadcastReceiver {

    private static volatile boolean sRegisterFlag = false;

    /** 系统语种 */
    @Nullable
    private static volatile Locale sSystemLanguage;

    static void register(@NonNull Application application) {
        if (sRegisterFlag) {
            return;
        }
        sRegisterFlag = true;

        sSystemLanguage = LanguagesUtils.getSystemLocale(application);
        IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
        application.registerReceiver(new LocaleChangeReceiver(application), filter);
    }

    @NonNull
    private final Application mApplication;

    public LocaleChangeReceiver(@NonNull Application application) {
        mApplication = application;
    }

    @Override
    public void onReceive(@NonNull Context context, @Nullable Intent intent) {
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

        Locale systemLanguage = sSystemLanguage;
        if (systemLanguage == null) {
            return;
        }

        Locale latestSystemLocale = MultiLanguages.getSystemLanguage(mApplication);
        if (latestSystemLocale == null) {
            return;
        }
        if (MultiLanguages.equalsLanguageAndCountry(latestSystemLocale, systemLanguage)) {
            return;
        }

        notifySystemLocaleChange(systemLanguage, latestSystemLocale);
    }

    /**
     * 通知系统语种发生变化
     */
    public void notifySystemLocaleChange(@NonNull Locale oldLocale, @NonNull Locale newLocale) {
        sSystemLanguage = newLocale;

        final Application application = mApplication;
        // 如果当前的语种是跟随系统变化的，那么就需要重置一下当前 App 的语种
        if (LanguagesConfig.isSystemLanguage(application)) {
            LanguagesConfig.clearLanguageSetting(application);
        }

        OnLanguageListener listener = MultiLanguages.getOnLanguagesListener();
        if (listener == null) {
            return;
        }
        listener.onSystemLocaleChange(oldLocale, newLocale);
    }
}