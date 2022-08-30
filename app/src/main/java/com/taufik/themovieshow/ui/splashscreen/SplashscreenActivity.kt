package com.taufik.themovieshow.ui.splashscreen

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.taufik.themovieshow.databinding.ActivitySplashscreenBinding
import com.taufik.themovieshow.ui.MainActivity

class SplashscreenActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySplashscreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSplashscreen()
        setAppVersion()
    }

    private fun setSplashscreen() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, DELAY_TIME)
    }

    private fun setAppVersion() = with(binding) {
        try {
            val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            val appVersion = pInfo.versionName
            tvAppVersion.text = appVersion
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    companion object {
        const val DELAY_TIME = 1000L
    }
}