package com.hjq.language.demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/11/18
 *    desc   : Activity 管理器
 */
public final class ActivityManager implements Application.ActivityLifecycleCallbacks {

    private static volatile ActivityManager sInstance;

    /** Activity 存放集合 */
    private final List<Activity> mActivityList = new ArrayList<>();

    /** 应用生命周期回调 */
    private final ArrayList<ApplicationLifecycleCallback> mLifecycleCallbacks = new ArrayList<>();

    /** 当前应用上下文对象 */
    private Application mApplication;
    /** 栈顶的 Activity 对象 */
    private Activity mTopActivity;
    /** 前台并且可见的 Activity 对象 */
    private Activity mResumedActivity;

    private ActivityManager() {}

    public static ActivityManager getInstance() {
        if(sInstance == null) {
            synchronized (ActivityManager.class) {
                if(sInstance == null) {
                    sInstance = new ActivityManager();
                }
            }
        }
        return sInstance;
    }

    public void init(Application application) {
        mApplication = application;
        mApplication.registerActivityLifecycleCallbacks(this);
    }

    /**
     * 获取 Application 对象
     */
    @NonNull
    public Application getApplication() {
        return mApplication;
    }

    /**
     * 获取栈顶的 Activity
     */
    @Nullable
    public Activity getTopActivity() {
        return mTopActivity;
    }

    /**
     * 获取前台并且可见的 Activity
     */
    @Nullable
    public Activity getResumedActivity() {
        return mResumedActivity;
    }

    /**
     * 获取 Activity 集合
     */
    @NonNull
    public List<Activity> getActivityList() {
        return mActivityList;
    }

    /**
     * 判断当前应用是否处于前台状态
     */
    public boolean isForeground() {
        return getResumedActivity() != null;
    }

    /**
     * 注册应用生命周期回调
     */
    public void registerApplicationLifecycleCallback(ApplicationLifecycleCallback callback) {
        mLifecycleCallbacks.add(callback);
    }

    /**
     * 取消注册应用生命周期回调
     */
    public void unregisterApplicationLifecycleCallback(ApplicationLifecycleCallback callback) {
        mLifecycleCallbacks.remove(callback);
    }

    /**
     * 销毁指定的 Activity
     */
    public void finishActivity(Class<? extends Activity> clazz) {
        if (clazz == null) {
            return;
        }

        Iterator<Activity> iterator = mActivityList.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (!activity.getClass().equals(clazz)) {
                continue;
            }
            if (!activity.isFinishing()) {
                activity.finish();
            }
            iterator.remove();
        }
    }

    /**
     * 销毁所有的 Activity
     */
    public void finishAllActivities() {
        finishAllActivities((Class<? extends Activity>) null);
    }

    /**
     * 销毁所有的 Activity
     *
     * @param classArray            白名单 Activity
     */
    @SafeVarargs
    public final void finishAllActivities(Class<? extends Activity>... classArray) {
        Iterator<Activity> iterator = mActivityList.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            boolean whiteClazz = false;
            if (classArray != null) {
                for (Class<? extends Activity> clazz : classArray) {
                    if (activity.getClass().equals(clazz)) {
                        whiteClazz = true;
                    }
                }
            }
            if (whiteClazz) {
                continue;
            }
            // 如果不是白名单上面的 Activity 就销毁掉
            if (!activity.isFinishing()) {
                activity.finish();
            }
            iterator.remove();
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if (mActivityList.isEmpty()) {
            for (ApplicationLifecycleCallback callback : mLifecycleCallbacks) {
                callback.onApplicationCreate(activity);
            }
        }
        mActivityList.add(activity);
        mTopActivity = activity;
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (mTopActivity == activity && mResumedActivity == null) {
            for (ApplicationLifecycleCallback callback : mLifecycleCallbacks) {
                callback.onApplicationForeground(activity);
            }
        }
        mTopActivity = activity;
        mResumedActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        if (mResumedActivity == activity) {
            mResumedActivity = null;
        }
        if (mResumedActivity == null) {
            for (ApplicationLifecycleCallback callback : mLifecycleCallbacks) {
                callback.onApplicationBackground(activity);
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        mActivityList.remove(activity);
        if (mTopActivity == activity) {
            mTopActivity = null;
        }
        if (mActivityList.isEmpty()) {
            for (ApplicationLifecycleCallback callback : mLifecycleCallbacks) {
                callback.onApplicationDestroy(activity);
            }
        }
    }

    /**
     * 判断是否在主进程中
     */
    public static boolean isMainProcess(Context context) {
        String processName = getProcessName();
        if (TextUtils.isEmpty(processName)) {
            // 如果获取不到进程名称，那么则将它当做主进程
            return true;
        }
        return TextUtils.equals(processName, context.getPackageName());
    }

    /**
     * 获取当前进程名称
     */
    @SuppressLint("PrivateApi, DiscouragedPrivateApi")
    @Nullable
    public static String getProcessName() {
        String processName = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            processName = Application.getProcessName();
        } else {
            try {
                Class<?> activityThread = Class.forName("android.app.ActivityThread");
                Method currentProcessNameMethod = activityThread.getDeclaredMethod("currentProcessName");
                processName = (String) currentProcessNameMethod.invoke(null);
            } catch (ClassNotFoundException | ClassCastException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(processName)) {
            return processName;
        }

        // 利用 Linux 系统获取进程名
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("/proc/self/cmdline");
            byte[] buffer = new byte[256];
            int len = 0;
            int b;
            while ((b = inputStream.read()) > 0 && len < buffer.length) {
                buffer[len++] = (byte) b;
            }
            if (len > 0) {
                return new String(buffer, 0, len, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 应用生命周期回调
     */
    public interface ApplicationLifecycleCallback {

        /**
         * 第一个 Activity 创建了
         */
        void onApplicationCreate(Activity activity);

        /**
         * 最后一个 Activity 销毁了
         */
        void onApplicationDestroy(Activity activity);

        /**
         * 应用从前台进入到后台
         */
        void onApplicationBackground(Activity activity);

        /**
         * 应用从后台进入到前台
         */
        void onApplicationForeground(Activity activity);
    }
}