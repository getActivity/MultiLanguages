package com.hjq.language;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    static void inject(@NonNull Application application) {
        application.registerActivityLifecycleCallbacks(new ActivityLanguages());
    }

    @Override
    public void onActivityPreCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPostCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPreStarted(@NonNull Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPostStarted(@NonNull Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPreResumed(@NonNull Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPostResumed(@NonNull Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPrePaused(@NonNull Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPostPaused(@NonNull Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPreStopped(@NonNull Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPostStopped(@NonNull Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPreSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPostSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityPreDestroyed(@NonNull Activity activity) {
        refreshActivityAndApplicationLanguage(activity);
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        refreshApplicationLanguage(activity.getApplication());
    }

    @Override
    public void onActivityPostDestroyed(@NonNull Activity activity) {
        refreshApplicationLanguage(activity.getApplication());
    }

    /**
     * 刷新 Activity 和 Application 的语种
     */
    private void refreshActivityAndApplicationLanguage(@NonNull Activity activity) {
        MultiLanguages.updateAppLanguage(activity);
        refreshApplicationLanguage(activity.getApplication());
    }

    /**
     * 刷新 Application 的语种
     */
    private void refreshApplicationLanguage(@Nullable Application application) {
        if (application == null) {
            return;
        }
        MultiLanguages.updateAppLanguage(application);
    }
}