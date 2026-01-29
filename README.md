# SparkFusionSDK

[![](https://jitpack.io/v/Spark-Fusion/SparkFusionSDK.svg)](https://jitpack.io/#Spark-Fusion/SparkFusionSDK)
![Android](https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=flat&logo=kotlin&logoColor=white)

一个用于 Android 应用的隐私政策对话框 SDK，帮助开发者快速集成隐私政策展示功能。

## 功能特性

- ✅ **隐私政策对话框**：提供美观的隐私政策展示对话框
- ✅ **文本高亮**：自动高亮《隐私政策》文本，支持点击跳转
- ✅ **自定义应用名称**：支持自定义应用名称，自动生成个性化内容
- ✅ **轻量存储**：基于 SharedPreferences 记录是否已同意，已同意时直接回调不弹窗
- ✅ **撤回同意**：提供撤回隐私政策同意的确认弹窗，闭包回调
- ✅ **接口封装**：清晰的接口设计，实现细节不对外暴露（Repository 模式）
- ✅ **易于集成**：简单易用的 API，快速集成到项目中
- ✅ **不可取消对话框**：确保用户必须做出选择，符合隐私政策合规要求

## 环境要求

- Android SDK 29 (Android 10) 及以上
- Kotlin 1.9.0+
- Gradle 8.2.1+

## 集成方式

### 1. 添加依赖

在项目的 `build.gradle.kts` 中添加：

```kotlin
dependencies {
    implementation("com.github.Spark-Fusion:SparkFusionSDK:1.0.2")
}
```

### 2. 初始化 SDK

在 `Application` 类的 `onCreate()` 方法中初始化 SDK：

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SparkFusionSDK.initialize(this)
    }
}
```

**注意**：请确保在 `AndroidManifest.xml` 中注册了自定义的 Application 类。

## 使用方法

### 显示隐私政策对话框（推荐：每次启动都调用）

SDK 内部会记录用户是否已同意。**已同意时直接执行 `onAgree` 回调，不弹窗**；未同意时才展示对话框。因此可在启动页每次都调用，无需自行判断：

```kotlin
SparkFusionSDK.showPrivacyPolicyDialog(
    context = this,
    appname = "我的应用",
    onClickWeb = {
        // 点击《隐私政策》文本时的回调，可打开隐私政策网页
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://your-privacy-policy-url.com"))
        startActivity(intent)
    },
    onAgree = {
        // 用户同意（含已同意过直接回调的情况）
        Toast.makeText(this, "已同意隐私政策", Toast.LENGTH_SHORT).show()
        navigateToMainScreen()
    },
    onRefuse = {
        // 用户点击"拒绝"按钮
        finish()
    }
)
```

### 查询是否已同意

```kotlin
val agreed = SparkFusionSDK.isPrivacyPolicyAgreed(context)
```

### 撤回隐私政策同意

在设置等场景下，可展示撤回确认弹窗；用户确认后清除同意状态，并执行闭包回调：

```kotlin
SparkFusionSDK.showRevokePrivacyPolicyDialog(this) {
    Toast.makeText(this, "已撤回隐私政策同意，下次启动将再次展示", Toast.LENGTH_SHORT).show()
}
```

### 强制展示弹窗（如测试场景）

需要强制弹出隐私政策对话框时（例如测试），可传入 `forceShow = true`：

```kotlin
SparkFusionSDK.showPrivacyPolicyDialog(
    context = this,
    appname = getString(R.string.app_name),
    onClickWeb = { /* ... */ },
    onAgree = { /* ... */ },
    onRefuse = { /* ... */ },
    forceShow = true  // 已同意也会弹窗
)
```

### 完整示例（启动页）

```kotlin
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 每次启动都调用即可，已同意则直接 onAgree 不弹窗
        SparkFusionSDK.showPrivacyPolicyDialog(
            context = this,
            appname = getString(R.string.app_name),
            onClickWeb = {
                WebViewActivity.start(this, "隐私政策", "https://your-privacy-policy-url.com")
            },
            onAgree = {
                // 进入主流程（广告/主页等）
                loadAdOrNavigateToMain()
            },
            onRefuse = { finish() }
        )
    }
}
```

## API 文档

### `initialize(context: Context)`

初始化 SDK，必须在 Application 的 `onCreate()` 中调用。

**参数：**
- `context`: Application Context

**示例：**
```kotlin
SparkFusionSDK.initialize(applicationContext)
```

### `showPrivacyPolicyDialog(...)`

显示隐私政策对话框。若已同意过（轻量存储记录），则直接执行 `onAgree` 不弹窗。

**参数：**
- `context`: Context - 上下文对象
- `appname`: String - 应用名称，会显示在对话框标题中
- `onClickWeb`: () -> Unit - 点击《隐私政策》文本时的回调
- `onAgree`: () -> Unit - 用户同意后的回调（含已同意直接回调）
- `onRefuse`: () -> Unit - 用户点击「拒绝」按钮的回调
- `forceShow`: Boolean - 是否强制展示弹窗（默认 false），用于测试

**示例：**
```kotlin
SparkFusionSDK.showPrivacyPolicyDialog(
    context = this,
    appname = "我的应用",
    onClickWeb = { /* 处理点击事件 */ },
    onAgree = { /* 处理同意事件 */ },
    onRefuse = { /* 处理拒绝事件 */ }
)
```

### `isPrivacyPolicyAgreed(context: Context): Boolean`

查询用户是否已同意隐私政策（由 SDK 轻量存储记录）。

### `showRevokePrivacyPolicyDialog(context: Context, onRevoked: () -> Unit)`

展示撤回隐私政策同意的确认弹窗。用户确认撤回后清除同意状态并执行 `onRevoked` 回调。

**示例：**
```kotlin
SparkFusionSDK.showRevokePrivacyPolicyDialog(this) {
    // 撤回完成后的逻辑
}
```

## 对话框特性

- **不可取消**：隐私政策对话框设置为不可取消（点击外部和返回键都不会关闭），确保用户必须做出选择
- **文本高亮**：自动高亮所有《隐私政策》文本，颜色为 `#33aaff`（蓝色）
- **点击跳转**：点击高亮的《隐私政策》文本会触发 `onClickWeb` 回调，可在回调中打开网页或跳转页面
- **自动生成内容**：对话框内容会根据应用名称自动生成，包含隐私政策相关说明
- **Material Design**：采用 Material Design 设计风格，界面美观现代

### 对话框内容说明

- 标题：欢迎使用{应用名称}
- 内容：包含隐私政策说明，其中《隐私政策》文本会被高亮并可点击
- 按钮：提供「同意」和「拒绝」两个按钮

## 注意事项

1. **初始化时机**：必须在 Application 的 `onCreate()` 中初始化 SDK，且只能初始化一次
2. **Context 使用**：`showPrivacyPolicyDialog` 需要 Activity Context，不能使用 Application Context
3. **对话框行为**：隐私政策对话框默认不可取消，用户必须选择「同意」或「拒绝」，符合隐私政策合规要求
4. **存储方式**：SDK 使用 SharedPreferences（轻量存储）记录是否已同意，无需额外依赖
5. **文本高亮**：所有出现的《隐私政策》文本都会被高亮，支持多次点击
6. **线程安全**：SDK 采用单例模式，可在主线程调用

## 依赖库

- `androidx.core:core-ktx` - AndroidX Core KTX
- `androidx.appcompat:appcompat` - AndroidX AppCompat
- `com.google.android.material:material` - Material Design 组件

## 架构设计

SDK 采用 **Repository 模式**，将接口和实现分离：

- **ISparkFusionSDK**：公共接口，定义对外暴露的 API
- **SparkFusionSDK**：公共入口对象，外部通过此对象访问 SDK
- **SparkFusionSDKImpl**：内部实现（internal），实现细节不对外暴露

这种设计的好处：
- 接口与实现分离，便于维护和扩展
- 实现细节隐藏，提高封装性
- 易于测试，可以轻松替换实现

## 版本历史

### v1.0.2
- ✨ 轻量存储记录是否已同意隐私政策（SharedPreferences）
- ✨ 已同意时直接执行 `onAgree` 回调，不展示弹窗
- ✨ 新增 `isPrivacyPolicyAgreed(context)` 查询是否已同意
- ✨ 新增 `showRevokePrivacyPolicyDialog(context, onRevoked)` 撤回隐私政策同意弹窗
- ✨ `showPrivacyPolicyDialog` 新增可选参数 `forceShow`，用于测试强制弹窗

### v1.0.1
- 功能优化与维护

### v1.0.0
- ✨ 初始版本发布
- ✨ 支持隐私政策对话框显示
- ✨ 支持文本高亮和点击跳转
- ✨ Repository 模式架构设计

## 常见问题

### Q: 对话框可以自定义样式吗？
A: 当前版本暂不支持自定义样式，对话框采用 Material Design 设计。如需自定义样式，可以 fork 项目进行修改。

### Q: 如何判断用户是否已同意隐私政策？
A: 直接调用 `SparkFusionSDK.isPrivacyPolicyAgreed(context)`，由 SDK 内部轻量存储记录。

### Q: 用户想撤回同意怎么办？
A: 调用 `SparkFusionSDK.showRevokePrivacyPolicyDialog(context) { ... }`，会弹出确认对话框，用户确认后清除同意状态并在闭包中回调。

### Q: 可以在多个地方显示对话框吗？
A: 可以。推荐在启动页每次都调用 `showPrivacyPolicyDialog`，已同意时不会弹窗，只会执行 `onAgree` 回调。

### Q: 对话框支持国际化吗？
A: 当前版本暂不支持国际化，对话框文本为中文。如需支持多语言，可以 fork 项目进行扩展。

## 许可证

[在此添加许可证信息]

## 联系方式

如有问题或建议，请通过以下方式联系：

- GitHub Issues: [项目 Issues 地址]
- Email: [联系邮箱]

## 贡献

欢迎提交 Issue 和 Pull Request！

## 致谢

感谢以下开源项目的支持：
- [AndroidX](https://developer.android.com/jetpack/androidx) - Android 扩展库
