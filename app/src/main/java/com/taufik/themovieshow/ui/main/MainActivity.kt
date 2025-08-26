package com.taufik.themovieshow.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import com.taufik.themovieshow.utils.extensions.applySystemBarInsets
import com.taufik.themovieshow.utils.extensions.showSuccessToasty
import dagger.hilt.android.AndroidEntryPoint
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
    private val viewModel: MainViewModel by viewModels()

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

        lifecycleScope.launch {
            viewModel.languageChangedMessage.collect { isLanguageChanged  ->
                if (isLanguageChanged) {
                    showSuccessToasty(getString(R.string.tvSuccesfullyChangedLanguage))
                    viewModel.clearLanguageChangedFlag()
                }
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