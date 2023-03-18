package com.taufik.themovieshow.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setNavHost()
        setUpNavigationDestination()
    }

    private fun setNavHost() = with(binding) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.findNavController()
        navBottom.setupWithNavController(navController)
    }

    private fun setUpNavigationDestination() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailTvShowFragment -> showHideBottomNavigation(false)
                R.id.detailMovieFragment -> showHideBottomNavigation(false)
                R.id.discoverMovieFragment -> showHideBottomNavigation(false)
                R.id.discoverTvShowFragment -> showHideBottomNavigation(false)
                else -> showHideBottomNavigation(true)
            }
        }
    }

    private fun showHideBottomNavigation(isShow: Boolean) = with(binding) {
        navBottom.isVisible = isShow
    }
}