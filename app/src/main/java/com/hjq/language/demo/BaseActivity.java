package com.hjq.language.demo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.hjq.language.MultiLanguages;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        // 国际化适配（绑定语种）
        super.attachBaseContext(MultiLanguages.attach(newBase));
    }
}