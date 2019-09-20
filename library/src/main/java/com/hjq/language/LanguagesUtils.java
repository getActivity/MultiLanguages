package com.hjq.language;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2019/05/03
 *    desc   : 国际化工具类
 */
final class LanguagesUtils {

    /**
     * 判断上下文的语种和某个语种是否相同
     */
    static boolean equalsLanguages(Context context, Locale locale) {
        Configuration config = context.getResources().getConfiguration();

        // API 版本兼容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return config.getLocales().get(0).equals(locale);
        } else {
            return config.locale.equals(locale);
        }
    }

    /**
     * 更新当前 App 的语种
     */
    static Context updateLanguages(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        Locale.setDefault(locale);
        return context;
    }

    /**
     * 获取某个语种下的 Resources 对象
     */
    static Resources getLanguageResources(Context context, Locale locale) {
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            return context.createConfigurationContext(config).getResources();
        } else {
            config.locale = locale;
            return new Resources(context.getAssets(), context.getResources().getDisplayMetrics(), config);
        }
    }
}