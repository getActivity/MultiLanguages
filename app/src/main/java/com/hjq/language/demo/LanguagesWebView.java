package com.hjq.language.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hjq.language.MultiLanguages;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2019/08/10
 *    desc   : 修复语种的 WebView
 */
public final class LanguagesWebView extends WebView {

    public LanguagesWebView(@NonNull Context context) {
        this(context, null);
    }

    public LanguagesWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    public LanguagesWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 修复 WebView 初始化时会修改 Activity 语种配置的问题
        MultiLanguages.updateAppLanguage(context);
    }
}