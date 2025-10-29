# [中文文档](README.md)

# Language Switching Framework

* Project address: [Github](https://github.com/getActivity/MultiLanguages)

* [Click here to download demo apk directly](https://github.com/getActivity/MultiLanguages/releases/download/9.8/MultiLanguages.apk)

![](picture/dynamic_figure.gif)

#### Integration Steps

* If your project's Gradle configuration is `below 7.0`, add the following to your `build.gradle` file:

```groovy
allprojects {
    repositories {
        // JitPack remote repository: https://jitpack.io
        maven { url 'https://jitpack.io' }
    }
}
```

* If your Gradle configuration is `7.0 or above`, add the following to your `settings.gradle` file:

```groovy
dependencyResolutionManagement {
    repositories {
        // JitPack remote repository: https://jitpack.io
        maven { url 'https://jitpack.io' }
    }
}
```

* After configuring the remote repository, add the remote dependency in the `build.gradle` file under your app module:

```groovy
dependencies {
    // Language switching framework: https://github.com/getActivity/MultiLanguages
    implementation 'com.github.getActivity:MultiLanguages:9.8'
}
```

#### Initialize the Framework

* Initialize the framework in your Application class:

```java
public final class XxxApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the language switching framework
        MultiLanguages.init(this);
    }
}
```

* Override the attachBaseContext method in your Application:

```java
@Override
protected void attachBaseContext(Context base) {
    // Bind language
    super.attachBaseContext(MultiLanguages.attach(base));
}
```

* Override the attachBaseContext method in your **base class** BaseActivity:

```java
@Override
protected void attachBaseContext(Context newBase) {
    // Bind language
    super.attachBaseContext(MultiLanguages.attach(newBase));
}
```

* Any subclass of Context needs to override this method, including Service. This will not be repeated here.

* Note: Fragment does not need to override this method because it is not a subclass of Context.

#### Language Settings

```java
// Set the current language (returns true if the app needs to be restarted)
MultiLanguages.setAppLanguage(Context context, Locale locale);

// Get the current language
MultiLanguages.getAppLanguage(Context context);

// Follow system language (returns true if the app needs to be restarted)
MultiLanguages.clearAppLanguage(Context context);
```

#### Other APIs

```java
// Get the system language
MultiLanguages.getSystemLanguage(Context context);
// Whether to follow the system language
MultiLanguages.isSystemLanguage(Context context);

// Compare if two languages are the same (e.g., Simplified and Traditional Chinese, American and British English)
MultiLanguages.equalsLanguage(Locale locale1, Locale locale2);
// Compare if two languages are from the same region (e.g., Simplified Chinese in Mainland China, Traditional Chinese in Taiwan)
MultiLanguages.equalsCountry(Locale locale1, Locale locale2);

// Get a String in a specific language
MultiLanguages.getLanguageString(Context context, Locale locale, int stringId);
// Generate a Resources object for a specific language
MultiLanguages.generateLanguageResources(Context context, Locale locale);

// Update the language of Context
MultiLanguages.updateAppLanguage(Context context);
// Update the language of Resources
MultiLanguages.updateAppLanguage(Resources resources);

// Set the default language (the earlier, the better)
MultiLanguages.setDefaultLanguage(Locale locale);
```

#### Language Change Listener

```java
// Set language change listener
MultiLanguages.setOnLanguageListener(new OnLanguageListener() {

    @Override
    public void onAppLocaleChange(Locale oldLocale, Locale newLocale) {
        Log.d("MultiLanguages", "Detected app language change, old language: " + oldLocale + ", new language: " + newLocale);
    }

    @Override
    public void onSystemLocaleChange(Locale oldLocale, Locale newLocale) {
        Log.d("MultiLanguages", "Detected system language change, old language: " + oldLocale + ", new language: " + newLocale + ", following system: " + MultiLanguages.isSystemLanguage());
    }
});
```

#### Usage Example

```java
@Override
public void onClick(View v) {
    // Whether restart is needed
    boolean restart;
    switch (v.getId()) {
        // Follow system
        case R.id.btn_language_auto:
            restart = MultiLanguages.clearAppLanguage(this);
            break;
        // Simplified Chinese
        case R.id.btn_language_cn:
            restart = MultiLanguages.setAppLanguage(this, LocaleContract.getSimplifiedChineseLocale());
            break;
        // Traditional Chinese
        case R.id.btn_language_tw:
            restart = MultiLanguages.setAppLanguage(this, LocaleContract.getTraditionalChineseLocale());
            break;
        // English
        case R.id.btn_language_en:
            restart = MultiLanguages.setAppLanguage(this, LocaleContract.getEnglishLocale());
            break;
        default:
            restart = false;
            break;
    }

    if (restart) {
        // You can make full use of Activity transition animations, and set a fade effect when switching
        Intent intent = new Intent(this, MainActivity.class);
        // Github address: https://github.com/getActivity/MultiLanguages/issues/55
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_alpha_in, R.anim.activity_alpha_out);
        finish();
    }
}
```

## [Click here for Frequently Asked Questions](HelpDoc-en.md)

#### Other resources: [Complete List of Language Codes](https://github.com/championswimmer/android-locales)

#### Other Open Source Projects by the Author

* Android middle office: [AndroidProject](https://github.com/getActivity/AndroidProject)![](https://img.shields.io/github/stars/getActivity/AndroidProject.svg)![](https://img.shields.io/github/forks/getActivity/AndroidProject.svg)

* Android middle office kt version: [AndroidProject-Kotlin](https://github.com/getActivity/AndroidProject-Kotlin)![](https://img.shields.io/github/stars/getActivity/AndroidProject-Kotlin.svg)![](https://img.shields.io/github/forks/getActivity/AndroidProject-Kotlin.svg)

* Permissions framework: [XXPermissions](https://github.com/getActivity/XXPermissions) ![](https://img.shields.io/github/stars/getActivity/XXPermissions.svg) ![](https://img.shields.io/github/forks/getActivity/XXPermissions.svg)

* Toast framework: [Toaster](https://github.com/getActivity/Toaster)![](https://img.shields.io/github/stars/getActivity/Toaster.svg)![](https://img.shields.io/github/forks/getActivity/Toaster.svg)

* Network framework: [EasyHttp](https://github.com/getActivity/EasyHttp)![](https://img.shields.io/github/stars/getActivity/EasyHttp.svg)![](https://img.shields.io/github/forks/getActivity/EasyHttp.svg)

* Title bar framework: [TitleBar](https://github.com/getActivity/TitleBar)![](https://img.shields.io/github/stars/getActivity/TitleBar.svg)![](https://img.shields.io/github/forks/getActivity/TitleBar.svg)

* Floating window framework: [EasyWindow](https://github.com/getActivity/EasyWindow)![](https://img.shields.io/github/stars/getActivity/EasyWindow.svg)![](https://img.shields.io/github/forks/getActivity/EasyWindow.svg)

* Device compatibility framework：[DeviceCompat](https://github.com/getActivity/DeviceCompat) ![](https://img.shields.io/github/stars/getActivity/DeviceCompat.svg) ![](https://img.shields.io/github/forks/getActivity/DeviceCompat.svg)

* Shape view framework: [ShapeView](https://github.com/getActivity/ShapeView)![](https://img.shields.io/github/stars/getActivity/ShapeView.svg)![](https://img.shields.io/github/forks/getActivity/ShapeView.svg)

* Shape drawable framework: [ShapeDrawable](https://github.com/getActivity/ShapeDrawable)![](https://img.shields.io/github/stars/getActivity/ShapeDrawable.svg)![](https://img.shields.io/github/forks/getActivity/ShapeDrawable.svg)

* Gson parsing fault tolerance: [GsonFactory](https://github.com/getActivity/GsonFactory)![](https://img.shields.io/github/stars/getActivity/GsonFactory.svg)![](https://img.shields.io/github/forks/getActivity/GsonFactory.svg)

* Logcat viewing framework: [Logcat](https://github.com/getActivity/Logcat)![](https://img.shields.io/github/stars/getActivity/Logcat.svg)![](https://img.shields.io/github/forks/getActivity/Logcat.svg)

* Nested scrolling layout framework：[NestedScrollLayout](https://github.com/getActivity/NestedScrollLayout) ![](https://img.shields.io/github/stars/getActivity/NestedScrollLayout.svg) ![](https://img.shields.io/github/forks/getActivity/NestedScrollLayout.svg)

* Android version guide: [AndroidVersionAdapter](https://github.com/getActivity/AndroidVersionAdapter)![](https://img.shields.io/github/stars/getActivity/AndroidVersionAdapter.svg)![](https://img.shields.io/github/forks/getActivity/AndroidVersionAdapter.svg)

* Android code standard: [AndroidCodeStandard](https://github.com/getActivity/AndroidCodeStandard)![](https://img.shields.io/github/stars/getActivity/AndroidCodeStandard.svg)![](https://img.shields.io/github/forks/getActivity/AndroidCodeStandard.svg)

* Android resource summary：[AndroidIndex](https://github.com/getActivity/AndroidIndex) ![](https://img.shields.io/github/stars/getActivity/AndroidIndex.svg) ![](https://img.shields.io/github/forks/getActivity/AndroidIndex.svg)

* Android open source leaderboard: [AndroidGithubBoss](https://github.com/getActivity/AndroidGithubBoss)![](https://img.shields.io/github/stars/getActivity/AndroidGithubBoss.svg)![](https://img.shields.io/github/forks/getActivity/AndroidGithubBoss.svg)

* Studio boutique plugins: [StudioPlugins](https://github.com/getActivity/StudioPlugins)![](https://img.shields.io/github/stars/getActivity/StudioPlugins.svg)![](https://img.shields.io/github/forks/getActivity/StudioPlugins.svg)

* Emoji collection: [EmojiPackage](https://github.com/getActivity/EmojiPackage)![](https://img.shields.io/github/stars/getActivity/EmojiPackage.svg)![](https://img.shields.io/github/forks/getActivity/EmojiPackage.svg)

* China provinces json: [ProvinceJson](https://github.com/getActivity/ProvinceJson)![](https://img.shields.io/github/stars/getActivity/ProvinceJson.svg)![](https://img.shields.io/github/forks/getActivity/ProvinceJson.svg)

* Markdown documentation：[MarkdownDoc](https://github.com/getActivity/MarkdownDoc) ![](https://img.shields.io/github/stars/getActivity/MarkdownDoc.svg) ![](https://img.shields.io/github/forks/getActivity/MarkdownDoc.svg)

## License

```text
Copyright 2019 Huang JinQun

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```