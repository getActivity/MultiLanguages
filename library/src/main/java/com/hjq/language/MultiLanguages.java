package com.hjq.language;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import java.util.Locale;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2019/05/03
 *    desc   : 多语种适配
 */
public final class MultiLanguages {

    /** Application 对象 */
    private static Application sApplication;

    /** 语种变化监听对象 */
    private static OnLanguageListener sLanguageListener;

    /**
     * 初始化多语种框架
     */
    public static void init(Application application) {
        init(application, true);
    }

    public static void init(Application application, boolean inject) {
        sApplication = application;
        LanguagesObserver.register(application);
        if (inject) {
            ActivityLanguages.inject(application);
        }
    }

    /**
     * 在上下文的子类中重写 attachBaseContext 方法（用于更新 Context 的语种）
     */
    public static Context attach(Context context) {
        if (!LanguagesTools.getLocale(context).equals(LanguagesConfig.getAppLanguage(context))) {
            return LanguagesTools.attachLanguages(context, LanguagesConfig.getAppLanguage(context));
        }
        return context;
    }

    /**
     * 更新 Context 的语种
     */
    public static void updateAppLanguage(Context context) {
        updateAppLanguage(context.getResources());
    }

    /**
     * 更新 Resources 的语种
     */
    public static void updateAppLanguage(Resources resources) {
        if (!LanguagesTools.getLocale(resources.getConfiguration()).equals(LanguagesConfig.getAppLanguage(sApplication))) {
            LanguagesTools.updateLanguages(resources, LanguagesConfig.getAppLanguage(sApplication));
        }
    }

    /**
     * 获取 App 的语种
     */
    public static Locale getAppLanguage() {
        return LanguagesConfig.getAppLanguage(sApplication);
    }

    /**
     * 设置 App 的语种
     */
    public static boolean setAppLanguage(Context context, Locale newLocale) {
        if (LanguagesTools.getLocale(context).equals(newLocale)) {
            // 不需要重启
            return false;
        }

        Locale oldLocale = LanguagesTools.getLocale(context);
        // 更新 Activity 的语种
        LanguagesTools.updateLanguages(context.getResources(), newLocale);
        if (context != sApplication) {
            // 更新 Application 的语种
            LanguagesTools.updateLanguages(sApplication.getResources(), newLocale);
        }
        LanguagesConfig.setAppLanguage(context, newLocale);
        if (sLanguageListener != null) {
            sLanguageListener.onAppLocaleChange(oldLocale, newLocale);
        }
        // 需要重启
        return true;
    }

    /**
     * 获取系统的语种
     */
    public static Locale getSystemLanguage() {
        return LanguagesObserver.getSystemLanguage();
    }

    /**
     * 将 App 语种设置为系统语种
     */
    public static boolean setSystemLanguage(Context context) {
        LanguagesConfig.clearLanguage(context);
        if (!LanguagesTools.getLocale(context).equals(getSystemLanguage())) {
            LanguagesTools.updateLanguages(context.getResources(), getSystemLanguage());
            if (context != sApplication) {
                // 更新 Application 的语种
                LanguagesTools.updateLanguages(sApplication.getResources(), getSystemLanguage());
            }
            // 需要重启
            return true;
        }
        // 不需要重启
        return false;
    }

    /**
     * 对比两个语言是否是同一个语种（比如：中文的简体和繁体，英语的美式和英式）
     */
    public static boolean equalsLanguage(Locale locale1, Locale locale2) {
        return locale1.getLanguage().equals(locale2.getLanguage());
    }

    /**
     * 对比两个语言是否是同一个地方的（比如：中国大陆用的中文简体，中国台湾用的中文繁体）
     */
    public static boolean equalsCountry(Locale locale1, Locale locale2) {
        return equalsLanguage(locale1, locale2) && locale1.getCountry().equals(locale2.getCountry());
    }

    /**
     * 获取某个语种下的 String
     */
    public static String getLanguageString(Context context, Locale locale, int id) {
        return getLanguageResources(context, locale).getString(id);
    }

    /**
     * 获取某个语种下的 Resources 对象
     */
    public static Resources getLanguageResources(Context context, Locale locale) {
        return LanguagesTools.getLanguageResources(context, locale);
    }

    /**
     * 设置语种变化监听器
     */
    public static void setOnLanguageListener(OnLanguageListener listener) {
        sLanguageListener = listener;
    }

    static OnLanguageListener getOnLanguagesListener() {
        return sLanguageListener;
    }

    /**
     * 设置保存的 SharedPreferences 文件名
     */
    public static void setSharedPreferencesName(String name) {
        LanguagesConfig.setSharedPreferencesName(name);
    }
}