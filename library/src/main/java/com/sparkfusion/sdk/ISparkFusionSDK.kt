package com.sparkfusion.sdk

import android.content.Context

/**
 * SparkFusionSDK 公共接口
 */
interface ISparkFusionSDK {
    /**
     * 初始化SDK，使用Application context
     * @param context Application context
     */
    fun initialize(context: Context)

    /**
     * 启动页用户须知。若已同意过（轻量存储记录），则直接执行 onAgree 不弹窗。
     * @param context Context
     * @param appname 应用名称
     * @param onClickWeb 点击《隐私政策》链接回调
     * @param onAgree 同意回调
     * @param onRefuse 拒绝回调
     * @param forceShow 是否强制展示弹窗（用于测试），默认 false
     */
    fun showPrivacyPolicyDialog(
        context: Context,
        appname: String,
        onClickWeb: () -> Unit,
        onAgree: () -> Unit,
        onRefuse: () -> Unit,
        forceShow: Boolean = false,
    )

    /**
     * 是否已同意隐私政策（由轻量存储记录）
     */
    fun isPrivacyPolicyAgreed(context: Context): Boolean

    /**
     * 展示撤回隐私政策同意的确认弹窗，用户确认撤回后清除同意状态并执行 onRevoked。
     * @param context Context
     * @param onRevoked 撤回完成后的回调
     */
    fun showRevokePrivacyPolicyDialog(
        context: Context,
        onRevoked: () -> Unit,
    )
}
