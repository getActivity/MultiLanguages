package com.hjq.language;

import android.app.LocaleManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    @Nullable
    static Locale getLocale(@NonNull Context context) {
        Resources resources = context.getResources();
        if (resources == null) {
            return null;
        }
        return getLocale(resources.getConfiguration());
    }

    @Nullable
    static Locale getLocale(@Nullable Configuration config) {
        if (config == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList locales = config.getLocales();
            if (locales.isEmpty()) {
                return null;
            }
            return locales.get(0);
        } else  {
            return config.locale;
        }
    }

    /**
     * 设置语种对象
     */
    static void setLocale(@Nullable Configuration config, @Nullable Locale locale) {
        if (config == null || locale == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(locale);
            config.setLocales(localeList);
            // 修复在 Android 13 的联想平板上调用 setLocales 或者 setLocale 修改语种无效的问题
            // Github issue 地址：https://github.com/getActivity/MultiLanguages/pull/62
            if (!equalsLanguageAndCountry(locale, config.getLocales().get(0))) {
                config.locale = locale;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }

        // 某些语种（例如：阿拉伯语）默认需支持 RTL 特性，以便符合阿拉伯人的阅读习惯
        // Github issue 地址：https://github.com/getActivity/MultiLanguages/pull/62
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLayoutDirection(locale);
        }
    }

    /**
     * 获取系统的语种对象
     */
    @Nullable
    static Locale getSystemLocale(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 在 Android 13 上，不能用 Resources.getSystem() 来获取系统语种了
            // Android 13 上面新增了一个 LocaleManager 的语种管理类
            // 因为如果调用 LocaleManager.setApplicationLocales 会影响获取到的结果不准确
            // 所以应该得用 LocaleManager.getSystemLocales 来获取会比较精准
            LocaleManager localeManager = context.getSystemService(LocaleManager.class);
            if (localeManager != null) {
                LocaleList systemLocales = localeManager.getSystemLocales();
                if (systemLocales.isEmpty()) {
                    return null;
                }
                return systemLocales.get(0);
            }
        }

        Resources systemResources = Resources.getSystem();
        if (systemResources == null) {
            return null;
        }
        return LanguagesUtils.getLocale(systemResources.getConfiguration());
    }

    /**
     * 获取默认的语种环境
     */
    @Nullable
    static Locale getDefaultLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = LocaleList.getDefault();
            if (localeList.isEmpty()) {
                return null;
            }
            return localeList.get(0);
        } else {
            return Locale.getDefault();
        }
    }

    /**
     * 设置默认的语种环境（日期格式化会用到）
     */
    static void setDefaultLocale(@Nullable Context context) {
        if (context == null) {
            return;
        }
        Resources resources = context.getResources();
        if (resources == null) {
            return;
        }
        Configuration config = resources.getConfiguration();
        if (config == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.setDefault(config.getLocales());
        } else {
            Locale.setDefault(config.locale);
        }
    }

    /**
     * 绑定当前 App 的语种
     */
    static Context attachLanguages(@Nullable Context context, @Nullable Locale locale) {
        if (context == null) {
            return context;
        }
        Resources resources = context.getResources();
        if (resources == null) {
            return context;
        }
        Configuration config = new Configuration(resources.getConfiguration());
        setLocale(config, locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context = context.createConfigurationContext(config);
        } else {
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        }
        return context;
    }

    /**
     * 更新 Resources 语种
     */
    static void updateLanguages(@Nullable Resources resources, @Nullable Locale locale) {
        if (resources == null || locale == null) {
            return;
        }
        Configuration config = resources.getConfiguration();
        if (config == null) {
            return;
        }
        setLocale(config, locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    /**
     * 更新手机配置信息变化
     */
    static void updateConfigurationChanged(@Nullable Context context, @Nullable Configuration newConfig, @Nullable Locale appLanguage) {
        if (context == null || newConfig == null || appLanguage == null) {
            return;
        }
        Configuration config = new Configuration(newConfig);
        // 绑定当前语种到这个新的配置对象中
        setLocale(config, appLanguage);
        Resources resources = context.getResources();
        // 更新上下文的配置信息
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    /**
     * 生成某个语种下的 Resources 对象
     */
    @Nullable
    static Resources generateLanguageResources(@Nullable Context context, @Nullable Locale locale) {
        if (context == null || locale == null) {
            return null;
        }
        Configuration config = new Configuration();
        setLocale(config, locale);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return context.createConfigurationContext(config).getResources();
        }
        return new Resources(context.getAssets(), context.getResources().getDisplayMetrics(), config);
    }

    /**
     * 对比两个语言是否同一个语种（比如：中文有简体和繁体，但是它们都属于同一个语种）
     */
    public static boolean equalsLanguage(@Nullable Locale locale1, @Nullable Locale locale2) {
        if (locale1 == null || locale2 == null) {
            return false;
        }
        return TextUtils.equals(locale1.getLanguage(), locale2.getLanguage());
    }

    /**
     * 对比两个语言是否同一个语种并且同一个地方的（比如：中国大陆用的中文简体，中国台湾用的中文繁体）
     */
    public static boolean equalsLanguageAndCountry(@Nullable Locale locale1, @Nullable Locale locale2) {
        // Github issue 地址：https://github.com/getActivity/MultiLanguages/issues/63
        if (locale1 == null || locale2 == null) {
            return false;
        }
        return TextUtils.equals(locale1.getLanguage(), locale2.getLanguage()) &&
            TextUtils.equals(locale1.getCountry(), locale2.getCountry());
    }
}