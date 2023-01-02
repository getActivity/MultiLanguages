package com.hjq.language;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Locale;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2019/05/03
 *    desc   : 语种配置保存类
 */
final class LanguagesConfig {

    private static final String KEY_LANGUAGE = "key_language";
    private static final String KEY_COUNTRY = "key_country";

    private static String sSharedPreferencesName = "language_setting";

    private static volatile Locale sCurrentLanguage;
    
    @Nullable
    private static List<Locale> sSupportedLanguages;

    @Nullable
    private static Locale sDefaultLocale;

    static void setSharedPreferencesName(String name) {
        sSharedPreferencesName = name;
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(sSharedPreferencesName, Context.MODE_PRIVATE);
    }

    static void setAppLanguage(Context context, Locale locale) {
        sCurrentLanguage = locale;
        getSharedPreferences(context).edit()
                .putString(KEY_LANGUAGE, locale.getLanguage())
                .putString(KEY_COUNTRY, locale.getCountry())
                .apply();
    }

    static Locale getAppLanguage(Context context) {
        if (sCurrentLanguage == null) {
            String language = getSharedPreferences(context).getString(KEY_LANGUAGE, null);
            String country = getSharedPreferences(context).getString(KEY_COUNTRY, null);
            if (!TextUtils.isEmpty(language)) {
                sCurrentLanguage = new Locale(language, country);
            } else {
                sCurrentLanguage = getDefaultLanguage();
            }
        }
        return sCurrentLanguage;
    }

    public static boolean isSystemLanguage(Context context) {
        String language = getSharedPreferences(context).getString(KEY_LANGUAGE, null);
        return language == null || "".equals(language);
    }

    static void clearLanguage(Context context) {
        sCurrentLanguage = getDefaultLanguage();
        getSharedPreferences(context).edit()
                .remove(KEY_LANGUAGE)
                .remove(KEY_COUNTRY)
                .apply();
    }
    
    static void setDefaultLanguageIfNotSupport(List<Locale> supportedLocales, Locale defaultLocale) {
        sSupportedLanguages = supportedLocales;
        sDefaultLocale = defaultLocale;
    }
    
    public static Locale getDefaultLanguage() {
        Locale systemLanguage = LanguagesObserver.getSystemLanguage();
        if (sSupportedLanguages == null || sDefaultLocale == null) return systemLanguage;
        boolean supportSystemLanguage = false;
        for (Locale locale : sSupportedLanguages) {
            if (MultiLanguages.equalsLanguage(locale, systemLanguage)) {
                supportSystemLanguage = true;
            }
        }
        return supportSystemLanguage ? systemLanguage : sDefaultLocale;
    }
}
