package com.sparkfusion.sdk

/**
 * SparkFusionSDK 公共入口
 * 外部通过此对象访问 SDK 功能
 */
object SparkFusionSDK : ISparkFusionSDK by SparkFusionSDKImpl
