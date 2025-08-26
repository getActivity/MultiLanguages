#### Table of Contents

* [How to Set the Default Language of the App](#how-to-set-the-default-language-of-the-app)

* [What to Do If Switching Language Has No Effect](#what-to-do-if-switching-language-has-no-effect)

* [How to Apply Language Change to All Activities](#how-to-apply-language-change-to-all-activities)

* [How to Refresh All Screens When the User Switches System Language](#how-to-refresh-all-screens-when-the-user-switches-system-language)

* [What to Do If Returning to the App After System Language Switch Has No Effect](#what-to-do-if-returning-to-the-app-after-system-language-switch-has-no-effect)

* [Solution for WebView Causing Language Failure](#solution-for-webview-causing-language-failure)

* [What to Do If WebView Language Fails on Android 13](#what-to-do-if-webview-language-fails-on-android-13)

* [Is There a Way to Switch Language Without Restarting](#is-there-a-way-to-switch-language-without-restarting)

#### How to Set the Default Language of the App

* If you have never called `MultiLanguages.setAppLanguage` and do not want the app to follow the system language, but want to specify a certain language, you can do as follows:

```java
public final class XxxApplication extends Application {

    static {
        // Set the default language (the earlier, the better)
        MultiLanguages.setDefaultLanguage(LocaleContract.getEnglishLocale());
    }
}
```

* If you want to determine the current system language type and then set the default language, you can write:

```java
public final class XxxApplication extends Application {

    @Override
    protected void attachBaseContext(Context newBase) {
        if (newBase != null) {
            Locale systemLanguage = MultiLanguages.getSystemLanguage(newBase);
            // If the current language is neither Chinese (including Simplified and Traditional) nor English (including American and British), set it to English by default to avoid following the system language
            if (!MultiLanguages.equalsLanguage(systemLanguage, LocaleContract.getChineseLocale()) &&
                    !MultiLanguages.equalsLanguage(systemLanguage, LocaleContract.getEnglishLocale())) {
                MultiLanguages.setDefaultLanguage(LocaleContract.getEnglishLocale());
            }
        }
        // Bind language
        super.attachBaseContext(MultiLanguages.attach(newBase));
    }
}
```

#### What to Do If Switching Language Has No Effect

* Case 1: Check if you have configured to keep only certain country language resources in the `build.gradle` file, for example, `resConfigs 'zh'` means only Chinese-related language resources are kept, and other countries' language resources will not be packaged into the apk, which will cause the language to always be Chinese when switching.

* Case 2: If the language switch in Fragment has no effect, check if the Fragment is reused after the Activity restarts, usually caused by calling `fragment.setRetainInstance(true)`.

* Case 3: If you cannot switch the app language after uploading the aab package to Google Play or Huawei and downloading it, it is because multi-language resources are missing during splitting. You need to configure the main module's `build.gradle` file. For details, refer to the [official documentation](https://developer.android.google.cn/guide/app-bundle/configure-base?hl=en).

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

* Other cases: If the above reasons do not apply, please submit an [issue](https://github.com/getActivity/MultiLanguages/issues) for assistance.

#### How to Apply Language Change to All Activities

* Add the following code in the onCreate method of Application:

```java
MultiLanguages.setOnLanguageListener(new OnLanguageListener() {

    @Override
    public void onAppLocaleChange(Locale oldLocale, Locale newLocale) {
        Log.i("MultiLanguages", "Detected app language change, old language: " + oldLocale + ", new language: " + newLocale);
        // If you want the app to change with the system language, get all Activities here and call their recreate method
        // getAllActivity is just demonstration code, replace it with your own implementation. If not available, please encapsulate it yourself
        List<Activity> activityList = getAllActivity();
        for (Activity activity : activityList) {
            activity.recreate();
        }
    }

    @Override
    public void onSystemLocaleChange(Locale oldLocale, Locale newLocale) {
        Log.i("MultiLanguages", "Detected system language change, old language: " + oldLocale + ", new language: " + newLocale + ", following system: " + MultiLanguages.isSystemLanguage());
    }
});
```

#### How to Refresh All Screens When the User Switches System Language

* Add the following code in the onCreate method of Application:

```java
MultiLanguages.setOnLanguageListener(new OnLanguageListener() {

    @Override
    public void onAppLocaleChange(Locale oldLocale, Locale newLocale) {
        Log.i("MultiLanguages", "Detected app language change, old language: " + oldLocale + ", new language: " + newLocale);
    }

    @Override
    public void onSystemLocaleChange(Locale oldLocale, Locale newLocale) {
        Log.i("MultiLanguages", "Detected system language change, old language: " + oldLocale + ", new language: " + newLocale + ", following system: " + MultiLanguages.isSystemLanguage());

        // If the current language is not following the system, do not proceed
        if (!MultiLanguages.isSystemLanguage(AppApplication.this)) {
            return;
        }
        
        // If you want the app to change with the system language, get all Activities here and call their recreate method
        // getAllActivity is just demonstration code, replace it with your own implementation. If not available, please encapsulate it yourself
        List<Activity> activityList = getAllActivity();
        for (Activity activity : activityList) {
            activity.recreate();
        }
    }
});
```

#### What to Do If Returning to the App After System Language Switch Has No Effect

* Please check if the Activity is configured with the `android:configChanges` attribute. If so, remove it and try again. For documentation, see [`<activity>`](https://developer.android.com/guide/topics/manifest/activity-element?hl=en)

```xml
<activity
    android:name=".XxxActivity"
    android:configChanges="orientation|keyboardHidden|keyboard|screenSize|smallestScreenSize|locale|layoutDirection|fontScale|screenLayout|density|uiMode" />
```

* If you do not want to remove the `android:configChanges` attribute but want the language switch to take effect after switching the system language, add the following code in the onCreate method of Application:

```java
MultiLanguages.setOnLanguageListener(new OnLanguageListener() {

    @Override
    public void onAppLocaleChange(Locale oldLocale, Locale newLocale) {
        Log.i("MultiLanguages", "Detected app language change, old language: " + oldLocale + ", new language: " + newLocale);
    }

    @Override
    public void onSystemLocaleChange(Locale oldLocale, Locale newLocale) {
        Log.i("MultiLanguages", "Detected system language change, old language: " + oldLocale + ", new language: " + newLocale + ", following system: " + MultiLanguages.isSystemLanguage());

        // If the current language is not following the system, do not proceed
        if (!MultiLanguages.isSystemLanguage(AppApplication.this)) {
            return;
        }
        
        // If you want the app to change with the system language, get all Activities here and call their recreate method
        // getAllActivity is just demonstration code, replace it with your own implementation. If not available, please encapsulate it yourself
        List<Activity> activityList = getAllActivity();
        for (Activity activity : activityList) {
            activity.recreate();
        }
    }
});
```

#### Solution for WebView Causing Language Failure

* Since WebView initialization will modify the Activity language configuration, which indirectly causes the Activity language to be restored, you need to manually override WebView to fix this issue

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

        // Fix the issue where WebView initialization modifies the Activity language configuration
        MultiLanguages.updateAppLanguage(context);
    }
}
```

#### What to Do If WebView Language Fails on Android 13

* Replace `webView.loadUrl(@NonNull String url)` with `webView.loadUrl(@NonNull String url, @NonNull Map<String, String> additionalHttpHeaders)` and add the `Accept-Language` request header. Example code:

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
                // If this is a link jump
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
     * Add language environment to WebView request header
     */
    @NonNull
    public static Map<String, String> generateLanguageRequestHeader(Context context) {
        Map<String, String> map = new HashMap<>(1);
        // Solution for language failure on Android 13
        // https://developer.android.com/about/versions/13/features/app-languages#consider-header
        map.put("Accept-Language", String.valueOf(MultiLanguages.getAppLanguage(context)));
        return map;
    }
}
```

#### Is There a Way to Switch Language Without Restarting

* Let me ask everyone a question: once rice is cooked, how do you turn it back into raw rice? Obviously, this is unrealistic. Even if the framework could do it, and text and images could automatically change with the language, how would you switch the language for data requested via the network? Would you need to request again? If it's a list, would you need to request from page 1? Another question: is language switching a common action? I believe you already have the answer.

* So it's not that switching language without restarting is impossible, but it's unnecessary (language switching is not a common action), and there are some inherent issues (even if the UI doesn't need to change, the data layer still needs to be re-requested).