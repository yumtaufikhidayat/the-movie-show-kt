package com.taufik.themovieshow.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withResumed
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.taufik.themovieshow.R
import com.taufik.themovieshow.base.activity.BaseActivity
import com.taufik.themovieshow.databinding.ActivityMainBinding
import com.taufik.themovieshow.utils.analytics.AnalyticsEvent
import com.taufik.themovieshow.utils.analytics.AnalyticsTracker
import com.taufik.themovieshow.utils.extensions.applySystemBarInsets
import com.taufik.themovieshow.utils.extensions.getLocalizedString
import com.taufik.themovieshow.utils.extensions.showSuccessToasty
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var analyticsTracker: AnalyticsTracker

    private var navController: NavController? = null
    private val navControllerDestination = NavController.OnDestinationChangedListener { _, destination, _ ->
        when (destination.id) {
            R.id.splashScreenFragment,
            R.id.discoverMovieFragment,
            R.id.discoverTvShowFragment,
            R.id.detailMovieTvShowFragment -> showBottomNavigation(false)
            else -> showBottomNavigation(true)
        }
        logScreenView(destination)
    }
    private val viewModel: MainViewModel by viewModels()

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onActivityReady(savedInstanceState: Bundle?) {
        // The app is always dark, so keep light system bar icons even when the device uses light mode.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(NAVIGATION_BAR_SCRIM)
        )
        binding.root.applySystemBarInsets(
            applyTop = true,
            applyBottom = false,
            extraBottomTarget = binding.bottomInsets
        )

        setNavHost()
        setUpNavigationDestination()
        setSuccessChangeLanguageMessage()
    }

    private fun setNavHost() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.findNavController()
        navController?.let {
            binding.navBottom.setupWithNavController(it)
            binding.navBottom.setOnItemSelectedListener { item ->
                analyticsTracker.log(AnalyticsEvent.TabSelect(resources.getResourceEntryName(item.itemId)))
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

    private fun setSuccessChangeLanguageMessage() {
        viewModel.languageChangedMessage
            .filter { it }
            .onEach { isLanguageChanged ->
                if (isLanguageChanged) {
                    val message = applicationContext.getLocalizedString(R.string.tvSuccesfullyChangedLanguage)
                    showSuccessToasty(message)
                    viewModel.clearLanguageChangedFlag()
                }
            }
            .launchIn(lifecycleScope)
    }

    // Firebase drops screen_view while the activity is not resumed, e.g. for the start destination set in onCreate.
    private fun logScreenView(destination: NavDestination) {
        val screenName = resources.getResourceEntryName(destination.id)
        val screenClass = (destination as? FragmentNavigator.Destination)?.className
            ?.substringAfterLast('.')
            ?: screenName
        lifecycleScope.launch {
            withResumed { analyticsTracker.log(AnalyticsEvent.ScreenView(screenName, screenClass)) }
        }
    }

    private fun showBottomNavigation(isShow: Boolean) {
        binding.navBottom.isVisible = isShow
    }

    override fun onDestroy() {
        navController?.removeOnDestinationChangedListener(navControllerDestination)
        navController = null
        super.onDestroy()
    }

    companion object {
        // Same scrim enableEdgeToEdge() uses for the navigation bar in dark mode.
        private val NAVIGATION_BAR_SCRIM = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
    }
}
