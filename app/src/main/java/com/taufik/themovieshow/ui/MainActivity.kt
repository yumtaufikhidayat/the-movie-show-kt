package com.taufik.themovieshow.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.ActivityMainBinding
import com.taufik.themovieshow.utils.extensions.applySystemBarInsets
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
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
        setContentView(binding.root)
        enableEdgeToEdge()
        binding.root.applySystemBarInsets(
            applyTop = true,
            applyBottom = false,
            extraBottomTarget = binding.bottomInsets
        )

        setNavHost()
        setUpNavigationDestination()
    }

    private fun setNavHost() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.findNavController()
        navController?.let { binding.navBottom.setupWithNavController(it) }
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