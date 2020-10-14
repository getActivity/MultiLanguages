package com.hjq.language;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import java.util.Locale;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2019/05/03
 *    desc   : 国际化管理
 */
public final class MultiLanguages {

    /**
     * 初始化国际化框架
     */
    public static void init(Application application) {
        LanguagesChange.register(application);
    }

    /**
     * 在上下文的子类中重写 attachBaseContext 方法（用于更新 Context 的语种）
     */
    public static Context attach(Context context) {
        if (!LanguagesUtils.equalsLanguages(context, getAppLanguage(context))) {
            return LanguagesUtils.updateLanguages(context, getAppLanguage(context));
        }
        return context;
    }

    /**
     * 获取 App 的语种
     */
    public static Locale getAppLanguage(Context context) {
        return LanguagesSave.getAppLanguage(context);
    }

    /**
     * 设置 App 的语种
     */
    public static boolean setAppLanguage(Context context, Locale locale) {
        LanguagesSave.saveAppLanguage(context, locale);

        if (!LanguagesUtils.equalsLanguages(context, locale)) {
            // 更新当前和全局上下文的语种
            LanguagesUtils.updateLanguages(context, locale);
            if (context != context.getApplicationContext()) {
                LanguagesUtils.updateLanguages(context.getApplicationContext(), locale);
            }
            // 需要重启
            return true;
        }
        // 不需要重启
        return false;
    }

    /**
     * 获取系统的语种
     */
    public static Locale getSystemLanguage() {
        return LanguagesChange.getSystemLanguage();
    }

    /**
     * 将 App 语种设置为系统语种
     */
    public static boolean setSystemLanguage(Context context) {
        LanguagesSave.clearLanguage(context);
        if (!LanguagesUtils.equalsLanguages(context, getSystemLanguage())) {
            LanguagesUtils.updateLanguages(context, getSystemLanguage());
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
        return LanguagesUtils.getLanguageResources(context, locale);
    }

    /**
     * 设置保存的 SharedPreferences 文件名
     */
    public static void setSharedPreferencesName(String name) {
        LanguagesSave.setSharedPreferencesName(name);
    }
}