package com.taufik.themovieshow.base.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.taufik.themovieshow.utils.language.ContextUtils
import com.taufik.themovieshow.utils.language.LanguageUtil

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun inflateBinding(layoutInflater: LayoutInflater): VB
    abstract fun onActivityReady(savedInstanceState: Bundle?)

    override fun attachBaseContext(newBase: Context) {
        val locale = LanguageUtil.getCurrentLocaleBlocking(newBase)
        val updatedContext = ContextUtils.Companion.updateLocale(newBase, locale)
        super.attachBaseContext(updatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateBinding(layoutInflater)
        setContentView(binding.root)
        onActivityReady(savedInstanceState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}