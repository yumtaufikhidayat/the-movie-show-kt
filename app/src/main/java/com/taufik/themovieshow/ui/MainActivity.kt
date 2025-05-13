package com.taufik.themovieshow.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.taufik.themovieshow.R
import com.taufik.themovieshow.base.BaseActivity
import com.taufik.themovieshow.databinding.ActivityMainBinding
import com.taufik.themovieshow.ui.language.bottomsheet.LanguageBottomSheetDialog.Companion.LANGUAGE_CHANGED
import com.taufik.themovieshow.ui.language.bottomsheet.LanguageBottomSheetDialog.Companion.SUCCESS_CHANGE_LANGUAGE
import com.taufik.themovieshow.utils.extensions.applySystemBarInsets
import com.taufik.themovieshow.utils.extensions.showSuccessToasty
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private var navController: NavController? = null
    private val navControllerDestination = NavController.OnDestinationChangedListener { _, destination, _ ->
        when (destination.id) {
            R.id.splashScreenFragment,
            R.id.detailMovieFragment,
            R.id.detailTvShowFragment,
            R.id.discoverMovieFragment,
            R.id.discoverTvShowFragment -> showBottomNavigation(false)
            else -> showBottomNavigation(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.getBooleanExtra(SUCCESS_CHANGE_LANGUAGE, false)) {
            showSuccessToasty(getString(R.string.tvSuccesfullyChangedLanguage))
            intent.removeExtra(SUCCESS_CHANGE_LANGUAGE)
            lifecycleScope.launch {
                delay(100)
                navController?.navigate(R.id.movieFragment)
            }
        }
    }

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onActivityReady(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        binding.root.applySystemBarInsets(
            applyTop = true,
            applyBottom = false,
            extraBottomTarget = binding.bottomInsets
        )

        setNavHost()
        setUpNavigationDestination()

        if (intent.getBooleanExtra(LANGUAGE_CHANGED, false)) {
            lifecycleScope.launch {
                delay(100)
                navController?.navigate(R.id.movieFragment)
            }
        }
    }

    private fun setNavHost() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.findNavController()
        navController?.let {
            binding.navBottom.setupWithNavController(it)
            binding.navBottom.setOnItemSelectedListener { item ->
                val currentDestination = it.currentDestination?.id
                if (item.itemId == currentDestination) {
                    it.popBackStack(item.itemId, inclusive = true)
                    it.navigate(item.itemId)
                    true
                } else {
                    NavigationUI.onNavDestinationSelected(item, it)
                    true
                }
            }
        }
    }

    private fun setUpNavigationDestination() {
        navController?.addOnDestinationChangedListener(navControllerDestination)
    }

    private fun showBottomNavigation(isShow: Boolean) {
        binding.navBottom.isVisible = isShow
    }

    override fun onDestroy() {
        navController?.removeOnDestinationChangedListener(navControllerDestination)
        navController = null
        super.onDestroy()
    }
}