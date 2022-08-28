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

    private lateinit var binding: ActivitySplashscreenBinding
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSplashscreen()

        setAppVersion()
    }

    private fun setSplashscreen() {
        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }

    private fun setAppVersion() {
        binding.apply {
            try {
                val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
                val appVersion = pInfo.versionName
                tvAppVersion.text = appVersion
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }
    }
}