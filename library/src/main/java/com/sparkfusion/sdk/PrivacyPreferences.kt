package com.sparkfusion.sdk

import android.content.Context
import android.content.SharedPreferences

/**
 * 轻量存储：记录用户是否已同意隐私政策
 */
internal object PrivacyPreferences {
    private const val PREFS_NAME = "sparkfusion_privacy"
    private const val KEY_AGREED = "privacy_policy_agreed"

    private fun prefs(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isAgreed(context: Context): Boolean {
        return prefs(context).getBoolean(KEY_AGREED, false)
    }

    fun setAgreed(context: Context, agreed: Boolean) {
        prefs(context).edit().putBoolean(KEY_AGREED, agreed).apply()
    }

    fun clearAgreed(context: Context) {
        setAgreed(context, false)
    }
}
