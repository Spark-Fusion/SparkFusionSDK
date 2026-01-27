package com.sparkfusion.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.sparkfusion.sdk.SparkFusionSDK

class Application : Application(){

    companion object {
        lateinit var instance: Application
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        //初始化

        SparkFusionSDK.initialize(this)
    }
}