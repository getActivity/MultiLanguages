package com.hjq.language.demo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.hjq.language.MultiLanguages;
import com.hjq.language.OnLanguageListener;
import com.hjq.toast.Toaster;
import java.util.List;
import java.util.Locale;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2019/08/10
 *    desc   : 应用入口
 */
public final class AppApplication extends Application {

//    static {
//        // 设置默认的语种（越早设置越好）
//        MultiLanguages.setDefaultLanguage(LocaleContract.getEnglishLocale());
//    }

    @Override
    public void onCreate() {
        super.onCreate();

        ActivityManager.getInstance().init(this);

        // 初始化 Toast 框架
        Toaster.init(this);

        // 初始化多语种框架
        MultiLanguages.init(this);
        // 设置语种变化监听器
        MultiLanguages.setOnLanguageListener(new OnLanguageListener() {

            @Override
            public void onAppLocaleChange(Locale oldLocale, Locale newLocale) {
                Log.i("MultiLanguages", "监听到应用切换了语种，旧语种：" + oldLocale + "，新语种：" + newLocale);
            }

            @Override
            public void onSystemLocaleChange(Locale oldLocale, Locale newLocale) {
                Log.i("MultiLanguages", "监听到系统切换了语种，旧语种：" + oldLocale + "，新语种：" + newLocale +
                        "，是否跟随系统：" + MultiLanguages.isSystemLanguage(AppApplication.this));

                if (!MultiLanguages.isSystemLanguage(AppApplication.this)) {
                    return;
                }
                // 如需在系统切换语种后应用也要随之变化的，可以在这里获取所有的 Activity 并调用它的 recreate 方法
                // getAllActivity 只是演示代码，需要自行替换成项目已实现的方法，若项目中没有，请自行封装
                List<Activity> activityList = ActivityManager.getInstance().getActivityList();
                for (Activity activity : activityList) {
                    activity.recreate();
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        // 绑定语种
        super.attachBaseContext(MultiLanguages.attach(newBase));
    }
}