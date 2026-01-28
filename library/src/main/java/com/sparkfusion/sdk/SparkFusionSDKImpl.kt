package com.sparkfusion.sdk

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.sparkfusion.sdk.databinding.DialogPrivacyPolicyBinding

/**
 * SparkFusionSDK 内部实现
 * 实现细节不对外暴露
 */
internal object SparkFusionSDKImpl : ISparkFusionSDK {

    private val TAG="SparkFusionSDK"
    private lateinit var spannableString: SpannableString
    private val highlightText = "《隐私政策》"
    /**
     * @param context
     */
    override fun initialize(context: Context) {
        Log.d(TAG,"${context}初始化成功")
    }

    /**
     * 启动页用户须知
     */
    override fun showPrivacyPolicyDialog(
        context: Context,
        appname: String,
        onClickWeb: () -> Unit,
        onAgree: () -> Unit,
        onRefuse: () -> Unit,
    ) {
        // 获取 ViewBinding
        val binding = DialogPrivacyPolicyBinding.inflate(LayoutInflater.from(context))
        val dialog = Dialog(context, R.style.dialog_center).apply {
            // 设置对话框内容视图
            setContentView(binding.root)
            // 点击外部不消失
            setCancelable(false)
            // 返回键不消失
            setCanceledOnTouchOutside(false)
        }
        binding.title.text = "欢迎使用${appname}"
        val fullText = "\u3000\u3000在使用${appname}前，请您充分阅读并理解${appname}《隐私政策》条约;\n\u3000\u30001.保护用户隐私是我们一项基本政策，${appname}不会泄露您的个人信息;\n\u3000\u30002.我们会根据您使用的具体功能需要，收集必要的用户信息(如申请设备信息，存储等相关权限);\n\u3000\u30003.在您同意${appname}《隐私政策》后，我们将进行集成SDK的初始化工作，会收集您的android_id、Mac地址、IMEI和应用安装列表，以保障App正常数据统计和安全风控;\n\u3000\u30004.您可以阅读完整版的《隐私政策》了解我们申请使用相关权限的情况，以及对您个人隐私的保护措施。"
        spannableString = SpannableString(fullText)

        getHighLightText(fullText, onClick = {
            onClickWeb()
        })
        // 应用到TextView
        binding.content.apply {
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance() // 必须设置
            highlightColor = Color.TRANSPARENT // 移除点击背景色
        }

        binding.btnRefuse.setOnClickListener {
            onRefuse()
            dialog.dismiss()
        }

        binding.btnAgree.setOnClickListener {
            onAgree()
            dialog.dismiss()
        }

        // 显示对话框
        dialog.show()
    }


    private fun getHighLightText(
        content: String,
        onClick: () -> Unit
    ) {
        var lastIndex = 0
        val colorSpan = ForegroundColorSpan(Color.parseColor("#33aaff"))

        // 循环查找所有匹配项
        while (lastIndex >= 0) {
            val startIndex = content.indexOf(highlightText, lastIndex)
            if (startIndex == -1) break // 找不到更多匹配项

            val endIndex = startIndex + highlightText.length

            // 设置颜色
            spannableString.setSpan(
                colorSpan,
                startIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // 设置点击事件
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) = onClick()
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false // 去掉下划线
                }
            }
            spannableString.setSpan(
                clickableSpan,
                startIndex, endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            lastIndex = endIndex // 继续向后查找
        }
    }
}
