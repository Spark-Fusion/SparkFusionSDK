# SparkFusionSDK

一个用于 Android 应用的隐私政策对话框 SDK，帮助开发者快速集成隐私政策展示功能。

## 功能特性

- ✅ **隐私政策对话框**：提供美观的隐私政策展示对话框
- ✅ **文本高亮**：自动高亮《隐私政策》文本，支持点击跳转
- ✅ **自定义应用名称**：支持自定义应用名称，自动生成个性化内容
- ✅ **数据存储**：基于 MMKV 的高性能数据存储
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
    implementation("com.sparkfusion.sdk:SparkFusionSDK:1.0.0")
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

### 显示隐私政策对话框

在需要显示隐私政策的地方（如启动页、主页面）调用：

```kotlin
SparkFusionSDK.showPrivacyPolicyDialog(
    context = this,
    appname = "我的应用",
    onClickWeb = {
        // 点击《隐私政策》文本时的回调
        // 可以在这里打开隐私政策网页
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://your-privacy-policy-url.com"))
        startActivity(intent)
    },
    onAgree = {
        // 用户点击"同意"按钮的回调
        // 可以在这里保存用户同意状态，进入应用主界面等
        Toast.makeText(this, "已同意隐私政策", Toast.LENGTH_SHORT).show()
    },
    onRefuse = {
        // 用户点击"拒绝"按钮的回调
        // 可以在这里处理拒绝逻辑，如退出应用
        finish()
    }
)
```

### 完整示例

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // 检查是否已同意隐私政策
        if (!hasAgreedPrivacyPolicy()) {
            showPrivacyPolicyDialog()
        } else {
            navigateToMainScreen()
        }
    }
    
    private fun showPrivacyPolicyDialog() {
        SparkFusionSDK.showPrivacyPolicyDialog(
            context = this,
            appname = getString(R.string.app_name),
            onClickWeb = {
                // 打开隐私政策网页
                val url = "https://your-privacy-policy-url.com"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            },
            onAgree = {
                // 用户同意后的处理
                savePrivacyAgreementStatus(true)
                navigateToMainScreen()
            },
            onRefuse = {
                // 用户拒绝后的处理
                savePrivacyAgreementStatus(false)
                // 可以显示提示信息或退出应用
                Toast.makeText(this, "需要同意隐私政策才能使用应用", Toast.LENGTH_LONG).show()
                finish()
            }
        )
    }
    
    private fun hasAgreedPrivacyPolicy(): Boolean {
        // 检查用户是否已同意隐私政策
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("privacy_agreed", false)
    }
    
    private fun savePrivacyAgreementStatus(agreed: Boolean) {
        // 保存用户同意状态
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("privacy_agreed", agreed).apply()
    }
    
    private fun navigateToMainScreen() {
        // 导航到应用主界面
        // ...
    }
}
```

### 使用浏览器打开隐私政策页面

推荐使用系统浏览器或自定义 WebView 打开隐私政策页面：

```kotlin
SparkFusionSDK.showPrivacyPolicyDialog(
    context = this,
    appname = "我的应用",
    onClickWeb = {
        // 方式1: 使用系统浏览器打开
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://your-privacy-policy-url.com"))
        startActivity(intent)
        
        // 方式2: 使用自定义 WebView Activity 打开（需要自行实现）
        // WebViewActivity.start(this, "隐私政策", "https://your-privacy-policy-url.com")
    },
    onAgree = { /* ... */ },
    onRefuse = { /* ... */ }
)
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

显示隐私政策对话框。

**参数：**
- `context`: Context - 上下文对象
- `appname`: String - 应用名称，会显示在对话框标题中
- `onClickWeb`: () -> Unit - 点击《隐私政策》文本时的回调
- `onAgree`: () -> Unit - 用户点击"同意"按钮的回调
- `onRefuse`: () -> Unit - 用户点击"拒绝"按钮的回调

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

## 对话框特性

- **不可取消**：对话框设置为不可取消（点击外部和返回键都不会关闭），确保用户必须做出选择
- **文本高亮**：自动高亮所有《隐私政策》文本，颜色为 `#33aaff`（蓝色）
- **点击跳转**：点击高亮的《隐私政策》文本会触发 `onClickWeb` 回调，可在回调中打开网页或跳转页面
- **自动生成内容**：对话框内容会根据应用名称自动生成，包含隐私政策相关说明
- **Material Design**：采用 Material Design 设计风格，界面美观现代

### 对话框内容说明

对话框会自动生成以下内容：
- 标题：欢迎使用{应用名称}
- 内容：包含隐私政策说明，其中《隐私政策》文本会被高亮并可点击
- 按钮：提供"同意"和"拒绝"两个按钮

## 注意事项

1. **初始化时机**：必须在 Application 的 `onCreate()` 中初始化 SDK，且只能初始化一次
2. **Context 使用**：`showPrivacyPolicyDialog` 需要 Activity Context，不能使用 Application Context
3. **对话框行为**：对话框默认不可取消，用户必须选择"同意"或"拒绝"，符合隐私政策合规要求
4. **存储依赖**：SDK 使用 MMKV 进行数据存储，初始化时会自动初始化 MMKV
5. **文本高亮**：所有出现的《隐私政策》文本都会被高亮，支持多次点击
6. **线程安全**：SDK 采用单例模式，线程安全，可在任意线程调用

## 依赖库

- `com.tencent:mmkv:2.0.2` - 高性能键值存储
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

### v1.0.0
- ✨ 初始版本发布
- ✨ 支持隐私政策对话框显示
- ✨ 支持文本高亮和点击跳转
- ✨ 基于 MMKV 的数据存储
- ✨ Repository 模式架构设计

## 常见问题

### Q: 对话框可以自定义样式吗？
A: 当前版本暂不支持自定义样式，对话框采用 Material Design 设计。如需自定义样式，可以 fork 项目进行修改。

### Q: 如何判断用户是否已同意隐私政策？
A: SDK 不负责存储用户同意状态，需要开发者自行实现存储逻辑（可使用 SharedPreferences 或 MMKV）。

### Q: 可以在多个地方显示对话框吗？
A: 可以，但建议只在应用启动时显示一次，避免重复打扰用户。

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
- [MMKV](https://github.com/Tencent/MMKV) - 高性能键值存储
- [AndroidX](https://developer.android.com/jetpack/androidx) - Android 扩展库
