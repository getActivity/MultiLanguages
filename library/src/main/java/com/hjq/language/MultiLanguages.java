package com.hjq.language;

import android.app.Application;
import android.app.LocaleManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.LocaleList;
import android.os.Looper;
import android.os.MessageQueue;
import android.provider.Settings;
import android.text.TextUtils;
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

    public static void init(final Application application, boolean inject) {
        sApplication = application;
        LanguagesUtils.setDefaultLocale(application);
        if (inject) {
            ActivityLanguages.inject(application);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !isSystemLanguage(application)) {
            LocaleManager localeManager = application.getSystemService(LocaleManager.class);
            if (localeManager != null) {
                localeManager.setApplicationLocales(new LocaleList(getAppLanguage()));
            }
        }
        // 等所有的任务都执行完了，再设置对系统语种的监听，用户不可能在这点间隙的时间完成切换语言的
        // 经过实践证明 IdleHandler 会在第一个 Activity attachBaseContext 之后调用的，所以没有什么问题
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                LanguagesObserver.register(application);
                return false;
            }
        });
    }

    /**
     * 在上下文的子类中重写 attachBaseContext 方法（用于更新 Context 的语种）
     */
    public static Context attach(Context context) {
        Locale locale = LanguagesConfig.readAppLanguageSetting(context);
        if (LanguagesUtils.getLocale(context).equals(locale)) {
            return context;
        }
        return LanguagesUtils.attachLanguages(context, locale);
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
        if (resources == null) {
            return;
        }
        if (LanguagesUtils.getLocale(resources.getConfiguration()).equals(getAppLanguage())) {
            return;
        }
        LanguagesUtils.updateLanguages(resources, getAppLanguage());
    }

    /**
     * 获取 App 的语种
     */
    public static Locale getAppLanguage() {
        return LanguagesConfig.readAppLanguageSetting(sApplication);
    }

    /**
     * 设置 App 的语种
     *
     * @return              语种是否发生改变了
     */
    public static boolean setAppLanguage(Context context, Locale newLocale) {
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        //      context.getSystemService(LocaleManager.class).setApplicationLocales(new LocaleList(newLocale));
        //}
        // 这里解释一下，在 Android 13 上为什么不用 LocaleManager.setApplicationLocales 来设置语种，原因如下：
        // 1. 调用此 API 会自动重启 Activity，而框架是将重启操作放到了外层给开发者去重启
        // 2. 上面说了，调用此 API 会重启 Activity，重启也就算了，还顺带闪了一下，这个不能忍
        // 3. 调用此 API 会触发 onConfigurationChanged 方法，会导致框架误判为系统切换了语种，从而回调了错误的监听给外层
        LanguagesConfig.saveAppLanguageSetting(context, newLocale);
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

        LanguagesUtils.setDefaultLocale(context);
        if (sLanguageListener != null) {
            sLanguageListener.onAppLocaleChange(oldLocale, newLocale);
        }
        return true;
    }

    /**
     * 获取系统的语种
     */
    public static Locale getSystemLanguage(Context context) {
        return LanguagesUtils.getSystemLocale(context);
    }

    /**
     * 是否跟随系统的语种
     */
    public static boolean isSystemLanguage(Context context) {
        return LanguagesConfig.isSystemLanguage(context);
    }

    /**
     * 跟随系统语种
     *
     * @return              语种是否发生改变了
     */
    public static boolean clearAppLanguage(Context context) {
        LanguagesConfig.clearLanguageSetting(context);
        if (LanguagesUtils.getLocale(context).equals(getSystemLanguage(sApplication))) {
            return false;
        }

        LanguagesUtils.updateLanguages(context.getResources(), getSystemLanguage(sApplication));
        LanguagesUtils.setDefaultLocale(context);
        if (context != sApplication) {
            // 更新 Application 的语种
            LanguagesUtils.updateLanguages(sApplication.getResources(), getSystemLanguage(sApplication));
        }
        return true;
    }

    /**
     * 设置默认的语种（越早设置越好）
     */
    public static void setDefaultLanguage(Locale locale) {
        LanguagesConfig.setDefaultLanguage(locale);
    }

    /**
     * 对比两个语言是否是同一个语种（比如：中文有简体和繁体，但是它们都属于同一个语种）
     */
    public static boolean equalsLanguage(Locale locale1, Locale locale2) {
        return TextUtils.equals(locale1.getLanguage(), locale2.getLanguage());
    }

    /**
     * 对比两个语言是否是同一个地方的（比如：中国大陆用的中文简体，中国台湾用的中文繁体）
     */
    public static boolean equalsCountry(Locale locale1, Locale locale2) {
        return equalsLanguage(locale1, locale2) &&
                TextUtils.equals(locale1.getCountry(), locale2.getCountry());
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
     * 设置保存的 SharedPreferences 文件名（请在 Application 初始化之前设置，可以放在 Application 中的代码块或者静态代码块）
     */
    public static void setSharedPreferencesName(String name) {
        LanguagesConfig.setSharedPreferencesName(name);
    }

    /**
     * 获取语种系统设置界面（Android 13 及以上才有的）
     */
    public static Intent getLanguageSettingIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent intent = new Intent(Settings.ACTION_APP_LOCALE_SETTINGS);
            intent.setData(Uri.parse("package:" + sApplication.getPackageName()));
            if (LanguagesUtils.areActivityIntent(sApplication, intent)) {
                return intent;
            }
        }
        return null;
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