package com.hjq.language;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2021/01/21
 *    desc   : Activity 语种注入
 */
final class ActivityLanguages implements Application.ActivityLifecycleCallbacks {

    /*
    这里解释一下，为什么要在 Activity 所有生命周期中刷新语种
    这是因为发现有的手机厂商系统（例如 miui 系统）会偷摸修改 Activity 或 Application 绑定的语种
    Github 地址：https://github.com/getActivity/MultiLanguages/issues/52
     */

    static void inject(Application application) {
        application.registerActivityLifecycleCallbacks(new ActivityLanguages());
    }

    @Override
    public void onActivityPreCreated(Activity activity, Bundle savedInstanceState) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPostCreated(Activity activity, Bundle savedInstanceState) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPreStarted(Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPostStarted(Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPreResumed(Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPostResumed(Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPrePaused(Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPostPaused(Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPreStopped(Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPostStopped(Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPreSaveInstanceState(Activity activity, Bundle outState) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPostSaveInstanceState(Activity activity, Bundle outState) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPreDestroyed(Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        refreshApplicationLanguage(activity.getApplication());
    }

    @Override
    public void onActivityPostDestroyed(Activity activity) {
        refreshApplicationLanguage(activity.getApplication());
    }

    /**
     * 刷新 Activity 和 Application 的语种
     */
    private void refreshActivityAndApplicationLanguage(Activity activity) {
        if (activity == null) {
            return;
        }
        MultiLanguages.updateAppLanguage(activity);
        refreshApplicationLanguage(activity.getApplication());
    }

    /**
     * 刷新 Application 的语种
     */
    private void refreshApplicationLanguage(Application application) {
        if (application == null) {
            return;
        }
        MultiLanguages.updateAppLanguage(application);
    }
}