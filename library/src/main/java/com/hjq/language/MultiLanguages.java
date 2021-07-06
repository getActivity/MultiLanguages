package com.hjq.language;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import java.util.Locale;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2019/05/03
 *    desc   : 语种切换框架
 */
@SuppressWarnings("unused")
public final class MultiLanguages {

    /** 应用上下文对象 */
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
        LanguagesUtils.setDefaultLocale(application);
        if (inject) {
            ActivityLanguages.inject(application);
        }
    }

    /**
     * 在上下文的子类中重写 attachBaseContext 方法（用于更新 Context 的语种）
     */
    public static Context attach(Context context) {
        if (LanguagesUtils.getLocale(context).equals(LanguagesConfig.getAppLanguage(context))) {
            return context;
        }
        return LanguagesUtils.attachLanguages(context, LanguagesConfig.getAppLanguage(context));
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
        if (LanguagesUtils.getLocale(resources.getConfiguration()).equals(getAppLanguage())) {
            return;
        }
        LanguagesUtils.updateLanguages(resources, getAppLanguage());
    }

    /**
     * 获取 App 的语种
     */
    public static Locale getAppLanguage() {
        return LanguagesConfig.getAppLanguage(sApplication);
    }

    /**
     * 设置 App 的语种
     *
     * @return              语种是否发生改变了
     */
    public static boolean setAppLanguage(Context context, Locale newLocale) {
        if (LanguagesUtils.getLocale(context).equals(newLocale)) {
            return false;
        }

        Locale oldLocale = LanguagesUtils.getLocale(context);
        // 更新 Context 的语种
        LanguagesUtils.updateLanguages(context.getResources(), newLocale);
        if (context != sApplication) {
            // 更新 Application 的语种
            LanguagesUtils.updateLanguages(sApplication.getResources(), newLocale);
        }
        LanguagesConfig.setAppLanguage(context, newLocale);
        LanguagesUtils.setDefaultLocale(context);
        if (sLanguageListener != null) {
            sLanguageListener.onAppLocaleChange(oldLocale, newLocale);
        }
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
     *
     * @return              语种是否发生改变了
     */
    public static boolean setSystemLanguage(Context context) {
        LanguagesConfig.clearLanguage(context);
        if (LanguagesUtils.getLocale(context).equals(getSystemLanguage())) {
            return false;
        }

        LanguagesUtils.updateLanguages(context.getResources(), getSystemLanguage());
        LanguagesUtils.setDefaultLocale(context);
        if (context != sApplication) {
            // 更新 Application 的语种
            LanguagesUtils.updateLanguages(sApplication.getResources(), getSystemLanguage());
        }
        return true;
    }

    /**
     * 是否跟随系统的语种
     */
    public static boolean isSystemLanguage() {
        return LanguagesConfig.isSystemLanguage(sApplication);
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
        return LanguagesUtils.getLanguageResources(context, locale);
    }

    /**
     * 设置语种变化监听器
     */
    public static void setOnLanguageListener(OnLanguageListener listener) {
        sLanguageListener = listener;
    }

    /**
     * 设置保存的 SharedPreferences 文件名
     */
    public static void setSharedPreferencesName(String name) {
        LanguagesConfig.setSharedPreferencesName(name);
    }

    /**
     * 获取语种变化监听对象
     */
    static OnLanguageListener getOnLanguagesListener() {
        return sLanguageListener;
    }

    /**
     * 获取应用上下文
     */
    static Application getApplication() {
        return sApplication;
    }
}