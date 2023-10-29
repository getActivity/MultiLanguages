package com.hjq.language;

import android.app.LocaleManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
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

    /** 当前语种 */
    private static volatile Locale sCurrentLocale;

    /** 默认语种 */
    private static volatile Locale sDefaultLocale;

    static void setSharedPreferencesName(String name) {
        sSharedPreferencesName = name;
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(sSharedPreferencesName, Context.MODE_PRIVATE);
    }

    /**
     * 读取 App 语种
     */
    static Locale readAppLanguageSetting(Context context) {
        if (sCurrentLocale != null) {
            return sCurrentLocale;
        }

        String language = getSharedPreferences(context).getString(KEY_LANGUAGE, "");
        String country = getSharedPreferences(context).getString(KEY_COUNTRY, "");
        if (!TextUtils.isEmpty(language)) {
            sCurrentLocale = new Locale(language, country);
            return sCurrentLocale;
        }

        if (sDefaultLocale != null) {
            sCurrentLocale = sDefaultLocale;
            return sCurrentLocale;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 读取用户在《设置 > 系统 > 语言和输入法 > 应用语言》设置的语种
            LocaleManager localeManager = context.getSystemService(LocaleManager.class);
            if (localeManager != null) {
                sCurrentLocale = localeManager.getApplicationLocales().get(0);
                // 如果用户没用设置过，则获取到为 null
                if (sCurrentLocale != null) {
                    return sCurrentLocale;
                }
            }
        }

        sCurrentLocale = LanguagesUtils.getLocale(context);

        return sCurrentLocale;
    }

    /**
     * 保存 App 语种设置
     */
    static void saveAppLanguageSetting(Context context, Locale locale) {
        sCurrentLocale = locale;
        getSharedPreferences(context).edit()
                .putString(KEY_LANGUAGE, locale.getLanguage())
                .putString(KEY_COUNTRY, locale.getCountry())
                .apply();
    }

    /**
     * 清除语种设置
     */
    static void clearLanguageSetting(Context context) {
        sCurrentLocale = MultiLanguages.getSystemLanguage(context);
        getSharedPreferences(context).edit()
                .remove(KEY_LANGUAGE)
                .remove(KEY_COUNTRY)
                .apply();
    }

    /**
     * 是否跟随系统
     */
    public static boolean isSystemLanguage(Context context) {
        if (sDefaultLocale != null) {
            return false;
        }
        String language = getSharedPreferences(context).getString(KEY_LANGUAGE, "");
        return TextUtils.isEmpty(language);
    }

    /**
     * 设置默认的语种
     */
    public static void setDefaultLanguage(Locale locale) {
        if (sCurrentLocale != null) {
            // 这个 API 需要越早调用越好，建议放在 Application 静态代码块中初始化
            // 当然也可以在 Application 调用 super.attachBaseContext 方法之前
            throw new IllegalStateException("Please set this before application initialization");
        }
        sDefaultLocale = locale;
    }
}