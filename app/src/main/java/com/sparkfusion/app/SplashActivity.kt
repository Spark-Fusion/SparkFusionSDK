package com.sparkfusion.app

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.sparkfusion.app.application.SdkManager
import com.sparkfusion.app.databinding.ActivitySplashBinding
import com.sparkfusion.sdk.SparkFusionSDK
import com.sparkfusionad.sdk.SparkFusionAd

class SplashActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySplashBinding
    var canJumpImmediately: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 每次调用 showPrivacyPolicyDialog：已同意则直接 onAgree 不弹窗，未同意则弹窗
        SparkFusionSDK.showPrivacyPolicyDialog(
            this,
            appname = getString(R.string.app_name),
            onClickWeb = {
                WebViewActivity.start(this, getString(R.string.privacy_policy_title), getString(R.string.privacy_policy))
            },
            onAgree = {
                if (SdkManager.initAd()) {
                    SparkFusionAd.loadSFSplashAd(this)
                    SparkFusionAd.showSFSplashAd(binding.fl) { startActivity() }
                } else {
                    countdown()
                }
            },
            onRefuse = { finish() }
        )
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