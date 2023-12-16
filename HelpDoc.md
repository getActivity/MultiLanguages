#### 目录

* [如何设置 App 默认的语种](#如何设置-app-默认的语种)

* [切换语种后没有任何效果该怎么办](#切换语种后没有任何效果该怎么办)

* [怎么在切换语种后应用到所有 Activity 上](#怎么在切换语种后应用到所有-activity-上)

* [怎么在用户切换系统语种的时候重启 App](#怎么在用户切换系统语种的时候重启-app)

* [WebView 导致语种失效的解决方案](#webview-导致语种失效的解决方案)

* [Android 13 WebView 语种失效怎么办](#android-13-webview-语种失效怎么办)

* [上架 aab 包后语种切换失败了怎么处理](#上架-aab-包后语种切换失败了怎么处理)

* [有没有一种不用通过重启的方式来切换语种](#有没有一种不用通过重启的方式来切换语种)

#### 如何设置 App 默认的语种

* 在从来没有调用过 `MultiLanguages.setAppLanguage` 的情况下，又不想让应用跟随系统的语种，而是想指定某个语种该怎么做？具体写法示例如下：

```java
public final class XxxApplication extends Application {

    static {
        // 设置默认的语种（越早设置越好）
        MultiLanguages.setDefaultLanguage(LocaleContract.getEnglishLocale());
    }
}
```

* 当然你如果想判断当前的系统语种类型，然后再设置默认的语种，可以这样写：

```java
public final class XxxApplication extends Application {

    @Override
    protected void attachBaseContext(Context newBase) {
        if (newBase != null) {
            Locale systemLanguage = MultiLanguages.getSystemLanguage(newBase);
            // 如果当前语种既不是中文（包含简体和繁体）和英语（包含美式英式等），就默认设置成英文的，避免跟随系统语种
            if (!MultiLanguages.equalsLanguage(systemLanguage, LocaleContract.getChineseLocale()) &&
                    !MultiLanguages.equalsLanguage(systemLanguage, LocaleContract.getEnglishLocale())) {
                MultiLanguages.setDefaultLanguage(LocaleContract.getEnglishLocale());
            }
        }
        // 绑定语种
        super.attachBaseContext(MultiLanguages.attach(newBase));
    }
}
```

#### 切换语种后没有任何效果该怎么办

* 情况一：可以检查一下是否在 `build.gradle` 文件中配置了仅保留某个国家的语种资源，例如 `resConfigs 'zh'` 就代表只保留和中文相关的语种资源，而其他国家的语种资源就不会被打包进 apk 包中，这样就会导致在切换语种的时候始终都是中文的尴尬局面。

* 情况二：如果是 Fragment 里面的语种切换之后没有变化，请检查 Fragment 在 Activity 重启之后是否被复用了，一般情况下是调用了 `fragment.setRetainInstance(true)` 导致的。

* 情况三：如果是上架 GooglePlay 或者华为的 aab 包后，上架成功后再下载发现切换不了 App 语种，那是因为在分包的时候导致多语言资源缺失了，这时候需要对主模块的 `build.gradle` 文件进行配置，具体用法可以参考[官方文档](https://developer.android.google.cn/guide/app-bundle/configure-base?hl=zh-cn)

```groovy
android {

    ......

    bundle {

        ......

        language {
            enableSplit = false
        }

        ......
    }
}
```

* 其他情况：如果不是以上原因造成的，请提一个 [issue](https://github.com/getActivity/MultiLanguages/issues) 给到我处理。

#### 怎么在切换语种后应用到所有 Activity 上

* 在 Application 的 onCreate 方法中加入以下代码

```java
MultiLanguages.setOnLanguageListener(new OnLanguageListener() {

    @Override
    public void onAppLocaleChange(Locale oldLocale, Locale newLocale) {
        Log.i("MultiLanguages", "监听到应用切换了语种，旧语种：" + oldLocale + "，新语种：" + newLocale);
        // 如需在系统切换语种后应用也要随之变化的，可以在这里获取所有的 Activity 并调用它的 recreate 方法
        // getAllActivity 只是演示代码，需要自行替换成项目已实现的方法，若项目中没有，请自行封装
        List<Activity> activityList = getAllActivity();
        for (Activity activity : activityList) {
            activity.recreate();
        }
    }

    @Override
    public void onSystemLocaleChange(Locale oldLocale, Locale newLocale) {
        Log.i("MultiLanguages", "监听到系统切换了语种，旧语种：" + oldLocale + "，新语种：" + newLocale +
                "，是否跟随系统：" + MultiLanguages.isSystemLanguage());
    }
});
```

#### 怎么在用户切换系统语种的时候重启 App

* 在 Application 的 onCreate 方法中加入以下代码

```java
MultiLanguages.setOnLanguageListener(new OnLanguageListener() {

    @Override
    public void onAppLocaleChange(Locale oldLocale, Locale newLocale) {
        Log.i("MultiLanguages", "监听到应用切换了语种，旧语种：" + oldLocale + "，新语种：" + newLocale);
    }

    @Override
    public void onSystemLocaleChange(Locale oldLocale, Locale newLocale) {
        Log.i("MultiLanguages", "监听到系统切换了语种，旧语种：" + oldLocale + "，新语种：" + newLocale +
                "，是否跟随系统：" + MultiLanguages.isSystemLanguage());
        // 如需在系统切换语种后应用也要随之变化的，可以在这里获取所有的 Activity 并调用它的 recreate 方法
        // getAllActivity 只是演示代码，需要自行替换成项目已实现的方法，若项目中没有，请自行封装
        List<Activity> activityList = getAllActivity();
        for (Activity activity : activityList) {
            activity.recreate();
        }
    }
});
```

#### WebView 导致语种失效的解决方案

* 由于 WebView 初始化会修改 Activity 语种配置，间接导致 Activity 语种会被还原回去，所以需要你手动重写 WebView 对这个问题进行修复

```java
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
```

#### Android 13 WebView 语种失效怎么办

* 将 `webView.loadUrl(@NonNull String url)` 替换成 `webView.loadUrl(@NonNull String url, @NonNull Map<String, String> additionalHttpHeaders)`，并添加 `Accept-Language` 请求头即可，具体的示例代码如下：

```java
public final class MainActivity extends Activity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.wv_main_web);

        mWebView.setWebViewClient(new LanguagesViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl("https://developer.android.google.cn/kotlin", generateLanguageRequestHeader(this));
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
                    view.loadUrl(url, generateLanguageRequestHeader(view.getContext()));
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
    public static Map<String, String> generateLanguageRequestHeader(Context context) {
        Map<String, String> map = new HashMap<>(1);
        // Android 13 上面语种失效的问题解决方案
        // https://developer.android.google.cn/about/versions/13/features/app-languages?hl=zh-cn#consider-header
        map.put("Accept-Language", String.valueOf(MultiLanguages.getAppLanguage(context)));
        return map;
    }
}
```

#### 有没有一种不用通过重启的方式来切换语种

* 我先问大家一个问题，生米煮成熟饭了，怎么从熟饭变成生米？这显然是不现实的，退一万步讲，假设框架能做到，文字和图片都能自动跟随语种的变化而变化，那么通过接口请求的数据又怎么切换语种？是不是得重新请求？如果是列表数据是不是得从第 1 页开始请求？再问大家一个问题，还有语种切换是一个常用动作吗？我相信大家此时心里已经有了答案。

* 所以并不是做不到不用重启的效果，而是没有那个必要（切语种不是常用动作），并且存在一定的硬伤（虽然 UI 层不用动，但是数据层还是要重新请求）。