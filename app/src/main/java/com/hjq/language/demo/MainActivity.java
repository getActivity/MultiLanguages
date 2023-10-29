package com.hjq.language.demo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.language.LocaleContract;
import com.hjq.language.MultiLanguages;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/MultiLanguages
 *    time   : 2019/08/10
 *    desc   : 多语种切换演示
 */
public final class MainActivity extends BaseActivity
        implements RadioGroup.OnCheckedChangeListener, OnTitleBarListener {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.wv_main_web);

        TitleBar titleBar = findViewById(R.id.tb_main_bar);
        RadioGroup radioGroup = findViewById(R.id.rg_main_languages);

        titleBar.setOnTitleBarListener(this);

        mWebView.setWebViewClient(new LanguagesViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl("https://developer.android.google.cn/kotlin", generateLanguageRequestHeader());

        //((TextView) findViewById(R.id.tv_language_activity)).setText(this.getResources().getString(R.string.current_language));
        ((TextView) findViewById(R.id.tv_main_language_application)).setText(
                getApplication().getResources().getString(R.string.current_language));
        ((TextView) findViewById(R.id.tv_main_language_system)).setText(
                MultiLanguages.getLanguageString(this, MultiLanguages.getSystemLanguage(this), R.string.current_language));

        if (MultiLanguages.isSystemLanguage(this)) {
            radioGroup.check(R.id.rb_main_language_auto);
        } else {
            Locale locale = MultiLanguages.getAppLanguage();
            if (LocaleContract.getSimplifiedChineseLocale().equals(locale)) {
                radioGroup.check(R.id.rb_main_language_cn);
            } else if (LocaleContract.getTraditionalChineseLocale().equals(locale)) {
                radioGroup.check(R.id.rb_main_language_tw);
            } else if (LocaleContract.getEnglishLocale().equals(locale)) {
                radioGroup.check(R.id.rb_main_language_en);
            } else {
                radioGroup.check(R.id.rb_main_language_auto);
            }
        }

        radioGroup.setOnCheckedChangeListener(this);
    }

    /**
     * {@link RadioGroup.OnCheckedChangeListener}
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // 是否需要重启
        boolean restart = false;

        if (checkedId == R.id.rb_main_language_auto) {
            // 跟随系统
            restart = MultiLanguages.clearAppLanguage(this);
        } else if (checkedId == R.id.rb_main_language_cn) {
            // 简体中文
            restart = MultiLanguages.setAppLanguage(this, LocaleContract.getSimplifiedChineseLocale());
        } else if (checkedId == R.id.rb_main_language_tw) {
            // 繁体中文
            restart = MultiLanguages.setAppLanguage(this, LocaleContract.getTraditionalChineseLocale());
        } else if (checkedId == R.id.rb_main_language_en) {
            // 英语
            restart = MultiLanguages.setAppLanguage(this, LocaleContract.getEnglishLocale());
        }

        if (restart) {
            // 1.使用 recreate 来重启 Activity，效果差，有闪屏的缺陷
            // recreate();

            // 2.使用常规 startActivity 来重启 Activity，有从左向右的切换动画
            // 稍微比 recreate 的效果好一点，但是这种并不是最佳的效果
            // startActivity(new Intent(this, LanguageActivity.class));
            // finish();

            // 3.我们可以充分运用 Activity 跳转动画，在跳转的时候设置一个渐变的效果，相比前面的两种带来的体验是最佳的
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清除历史记录
        mWebView.clearHistory();
        //停止加载
        mWebView.stopLoading();
        //加载一个空白页
        mWebView.loadUrl("about:blank");
        mWebView.setWebChromeClient(null);
        mWebView.setWebViewClient(new WebViewClient());
        //移除WebView所有的View对象
        mWebView.removeAllViews();
        //销毁此的WebView的内部状态
        mWebView.destroy();
    }

    @Override
    public void onTitleClick(TitleBar titleBar) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://github.com/getActivity/MultiLanguages"));
        startActivity(intent);
    }

    public static class LanguagesViewClient extends WebViewClient {

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return shouldOverrideUrlLoading(view, request.getUrl().toString());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String scheme = Uri.parse(url).getScheme();
            if (scheme == null) {
                return false;
            }
            switch (scheme) {
                // 如果这是跳链接操作
                case "http":
                case "https":
                    view.loadUrl(url, generateLanguageRequestHeader());
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    /**
     * 给 WebView 请求头添加语种环境
     */
    @NonNull
    public static Map<String, String> generateLanguageRequestHeader() {
        Map<String, String> map = new HashMap<>(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 上面语种失效的问题解决方案
            // https://developer.android.google.cn/about/versions/13/features/app-languages?hl=zh-cn#consider-header
            map.put("Accept-Language", String.valueOf(MultiLanguages.getAppLanguage()));
        }
        return map;
    }
}