package com.hjq.language.demo;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.language.MultiLanguages;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2019/08/10
 *    desc   : Activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        // 国际化适配（绑定语种）
        super.attachBaseContext(MultiLanguages.attach(newBase));
    }
}