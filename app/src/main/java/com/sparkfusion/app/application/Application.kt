package com.sparkfusion.app.application

import android.app.Application
import com.sparkfusion.sdk.SparkFusionSDK

class Application : Application(){

    companion object {
        lateinit var instance: Application
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        AppContextHolder.init(this)
        //初始化(包含MMKV)
        SparkFusionSDK.initialize(this)
    }
}