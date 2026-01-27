package com.sparkfusion.sdk

import android.content.Context

/**
 * SparkFusionSDK 公共接口
 * 暴露给外部使用的 API
 */
interface ISparkFusionSDK {
    /**
     * 初始化SDK，使用Application context
     * @param context Application context
     */
    fun initialize(context: Context)

    /**
     * 启动页用户须知
     * @param context Context
     * @param appname 应用名称
     * @param content 自定义内容（可选）
     * @param privacyUrl 隐私政策URL
     * @param onAgreeText 同意按钮文本（可选）
     * @param onRefuseText 拒绝按钮文本（可选）
     * @param onAgree 同意回调
     * @param onRefuse 拒绝回调
     */
    fun showPrivacyPolicyDialog(
        context: Context,
        appname: String,
        onClickWeb: () -> Unit,
        onAgreeText: String? = null,
        onRefuseText: String? = null,
        onAgree: () -> Unit,
        onRefuse: () -> Unit,
    )
}
