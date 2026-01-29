package com.sparkfusion.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.sparkfusion.app.databinding.ActivityMainBinding
import com.sparkfusion.sdk.SparkFusionSDK
import com.sparkfusionad.sdk.SparkFusionAd

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 测试：强制展示隐私政策弹窗（用于验证弹窗内容与回调）
        binding.btnTestPrivacyDialog.setOnClickListener {
            SparkFusionSDK.showPrivacyPolicyDialog(
                this,
                appname = getString(R.string.app_name),
                onClickWeb = {
                    WebViewActivity.start(this, getString(R.string.privacy_policy_title), getString(R.string.privacy_policy))
                },
                onAgree = { Toast.makeText(this, "已同意隐私政策", Toast.LENGTH_SHORT).show() },
                onRefuse = { Toast.makeText(this, "已拒绝", Toast.LENGTH_SHORT).show() },
                forceShow = true,
            )
        }

        // 测试：撤回隐私政策同意，弹窗确认后闭包回调
        binding.btnRevokePrivacy.setOnClickListener {
            SparkFusionSDK.showRevokePrivacyPolicyDialog(this) {
                Toast.makeText(this, "已撤回隐私政策同意，下次启动将再次展示隐私政策", Toast.LENGTH_SHORT).show()
            }
        }

        binding.button.setOnClickListener {
            SparkFusionAd.showSFInterstitialAd(this, 1, true)
        }
        binding.button2.setOnClickListener {
            SparkFusionAd.showSFVideoAd(this, true, onAdLoadSuccess = {}, onAdLoadError = {}, onAdClose = {})
        }
        binding.button3.setOnClickListener {
            binding.fl.removeAllViews()
            SparkFusionAd.showSFBannerAd(binding.fl)
        }
    }
}