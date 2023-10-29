package com.hjq.language;

import android.app.LocaleManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import java.util.Locale;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2019/05/03
 *    desc   : 国际化工具类
 */
@SuppressWarnings("deprecation")
final class LanguagesUtils {

    /**
     * 获取语种对象
     */
    static Locale getLocale(Context context) {
        return getLocale(context.getResources().getConfiguration());
    }

    static Locale getLocale(Configuration config) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return config.getLocales().get(0);
        } else  {
            return config.locale;
        }
    }

    /**
     * 设置语种对象
     */
    static void setLocale(Configuration config, Locale locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(locale);
            config.setLocales(localeList);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
    }

    /**
     * 获取系统的语种对象
     */
    static Locale getSystemLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 在 Android 13 上，不能用 Resources.getSystem() 来获取系统语种了
            // Android 13 上面新增了一个 LocaleManager 的语种管理类
            // 因为如果调用 LocaleManager.setApplicationLocales 会影响获取到的结果不准确
            // 所以应该得用 LocaleManager.getSystemLocales 来获取会比较精准
            LocaleManager localeManager = context.getSystemService(LocaleManager.class);
            if (localeManager != null) {
                return localeManager.getSystemLocales().get(0);
            }
        }

        return LanguagesUtils.getLocale(Resources.getSystem().getConfiguration());
    }

    /**
     * 设置默认的语种环境（日期格式化会用到）
     */
    static void setDefaultLocale(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.setDefault(configuration.getLocales());
        } else {
            Locale.setDefault(configuration.locale);
        }
    }

    /**
     * 绑定当前 App 的语种
     */
    static Context attachLanguages(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        setLocale(config, locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context = context.createConfigurationContext(config);
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        return context;
    }

    /**
     * 更新 Resources 语种
     */
    static void updateLanguages(Resources resources, Locale locale) {
        Configuration config = resources.getConfiguration();
        setLocale(config, locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    /**
     * 更新手机配置信息变化
     */
    static void updateConfigurationChanged(Context context, Configuration newConfig) {
        Configuration config = new Configuration(newConfig);
        // 绑定当前语种到这个新的配置对象中
        setLocale(config, LanguagesConfig.readAppLanguageSetting(context));
        Resources resources = context.getResources();
        // 更新上下文的配置信息
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    /**
     * 获取某个语种下的 Resources 对象
     */
    static Resources getLanguageResources(Context context, Locale locale) {
        Configuration config = new Configuration();
        setLocale(config, locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return context.createConfigurationContext(config).getResources();
        }
        return new Resources(context.getAssets(), context.getResources().getDisplayMetrics(), config);
    }

    /**
     * 判断这个意图的 Activity 是否存在
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    static boolean areActivityIntent(Context context, Intent intent) {
        if (intent == null) {
            return false;
        }
        // 这里为什么不用 Intent.resolveActivity(intent) != null 来判断呢？
        // 这是因为在 OPPO R7 Plus （Android 5.0）会出现误判，明明没有这个 Activity，却返回了 ComponentName 对象
        PackageManager packageManager = context.getPackageManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return !packageManager.queryIntentActivities(intent,
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY)).isEmpty();
        }
        return !packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty();
    }
}