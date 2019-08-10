package com.hjq.language.demo;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;

import com.hjq.language.LanguagesManager;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        // 国际化适配（绑定语种）
        super.attachBaseContext(LanguagesManager.attach(newBase));
    }
}
