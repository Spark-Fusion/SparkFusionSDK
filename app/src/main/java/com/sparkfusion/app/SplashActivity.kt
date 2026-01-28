package com.sparkfusion.app

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.sparkfusion.app.application.SdkManager
import com.sparkfusion.app.config.AppConfig
import com.sparkfusion.app.databinding.ActivitySplashBinding
import com.sparkfusion.sdk.SparkFusionSDK
import com.sparkfusionad.sdk.SparkFusionAd

class SplashActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySplashBinding
    private lateinit var appConfig: AppConfig
    var canJumpImmediately: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appConfig = AppConfig()
        if (appConfig.getFirst()) {
            SparkFusionSDK.showPrivacyPolicyDialog(this, appname = getString(R.string.app_name),
                onClickWeb = {
                    WebViewActivity.start(this,getString(R.string.privacy_policy_title),getString(R.string.privacy_policy))
                },
                onAgree = {
                    if(SdkManager.initAd()){
                        SparkFusionAd.loadSFSplashAd( this)
                        SparkFusionAd.showSFSplashAd(binding.fl, {
                            startActivity()
                        })
                    }else{
                        countdown()
                    }
                    appConfig.setFirst(false)
                },
                onRefuse = {
                    finish()
                }
            )
        } else {
            //初始化插屏广告
            if(SdkManager.initAd()){
                SparkFusionAd.loadSFSplashAd( this)
                SparkFusionAd.showSFSplashAd(binding.fl, {
                    startActivity()
                })
            }else{
                countdown()
            }
        }
    }
    //倒计时
    private fun countdown() {
        val timer: CountDownTimer = object : CountDownTimer(
            2500, 10
        ) {
            override fun onTick(millisUntilFinished: Long) {
                //tv_time.setText(millisUntilFinished/1000+"秒");
            }

            override fun onFinish() {
                startActivity()

            }
        }
        timer.start()
    }
    //跳转到主页
    private fun startActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun jumpWhenCanClick() {
        if (canJumpImmediately) {
            startActivity()
        } else {
            canJumpImmediately = true
        }
    }

    override fun onPause() {
        super.onPause()
        canJumpImmediately = false;
    }

    override fun onResume() {
        super.onResume()
        if (canJumpImmediately) {
            jumpWhenCanClick();
        }
        canJumpImmediately = true;
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}