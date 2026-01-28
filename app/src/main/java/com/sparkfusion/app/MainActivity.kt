package com.sparkfusion.app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sparkfusion.app.databinding.ActivityMainBinding
import com.sparkfusion.sdk.SparkFusionSDK
import com.sparkfusionad.sdk.SparkFusionAd

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            SparkFusionAd.showSFInterstitialAd( this,1, true)
        }
        binding.button2.setOnClickListener {
            SparkFusionAd.showSFVideoAd( this, true, onAdLoadSuccess = {}, onAdLoadError = {}, onAdClose = {})
        }
        binding.button3.setOnClickListener {
            binding.fl.removeAllViews()
            SparkFusionAd.showSFBannerAd(binding.fl)
        }
    }
}