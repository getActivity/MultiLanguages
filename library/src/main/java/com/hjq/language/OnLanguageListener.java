package com.hjq.language;

import androidx.annotation.NonNull;
import java.util.Locale;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2021/01/18
 *    desc   : 语种变化监听器
 */
public interface OnLanguageListener {

    /**
     * 当前应用语种发生变化时回调
     *
     * @param oldLocale         旧语种
     * @param newLocale         新语种
     */
    void onAppLocaleChange(@NonNull Locale oldLocale, @NonNull Locale newLocale);

    /**
     * 手机系统语种发生变化时回调
     *
     * @param oldLocale         旧语种
     * @param newLocale         新语种
     */
    void onSystemLocaleChange(@NonNull Locale oldLocale, @NonNull Locale newLocale);
}