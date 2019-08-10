package com.hjq.language.demo;

import android.app.Application;
import android.content.Context;

import com.hjq.language.LanguagesManager;
import com.hjq.toast.ToastUtils;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
        // 在 Application 中初始化
        LanguagesManager.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        // 国际化适配（绑定语种）
        super.attachBaseContext(LanguagesManager.attach(base));
    }
}