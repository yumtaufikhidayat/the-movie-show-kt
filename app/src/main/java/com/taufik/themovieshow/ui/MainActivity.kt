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
import com.taufik.themovieshow.base.activity.BaseActivity
import com.taufik.themovieshow.databinding.ActivityMainBinding
import com.taufik.themovieshow.ui.language.bottomsheet.LanguageBottomSheetDialog.Companion.SUCCESS_CHANGE_LANGUAGE
import com.taufik.themovieshow.utils.extensions.applySystemBarInsets
import com.taufik.themovieshow.utils.extensions.navigateReplacingSplash
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
            R.id.discoverMovieFragment,
            R.id.discoverTvShowFragment,
            R.id.detailMovieTvShowFragment -> showBottomNavigation(false)
            else -> showBottomNavigation(true)
        }
    }
    private var hasHandledLanguageChange = false

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

        if (!hasHandledLanguageChange && intent.getBooleanExtra(SUCCESS_CHANGE_LANGUAGE, false)) {
            hasHandledLanguageChange = true
            showSuccessToasty(getString(R.string.tvSuccesfullyChangedLanguage))
            intent.removeExtra(SUCCESS_CHANGE_LANGUAGE)
            intent.replaceExtras(Bundle())
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

    override fun onStart() {
        super.onStart()
        if (!hasHandledLanguageChange && intent.getBooleanExtra(SUCCESS_CHANGE_LANGUAGE, false)) {
            hasHandledLanguageChange = true

            showSuccessToasty(getString(R.string.tvSuccesfullyChangedLanguage))
            intent.removeExtra(SUCCESS_CHANGE_LANGUAGE)

            lifecycleScope.launch {
                while (navController?.currentDestination == null) { delay(NAVIGATION_DELAY) }
                delay(NAVIGATION_DELAY)
                navController?.navigateReplacingSplash(R.id.movieFragment)
            }
        }
    }


    override fun onDestroy() {
        navController?.removeOnDestinationChangedListener(navControllerDestination)
        navController = null
        super.onDestroy()
    }

    companion object {
        const val NAVIGATION_DELAY = 100L
    }
}