package com.taufik.themovieshow.ui.movie.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentMovieBinding
import com.taufik.themovieshow.ui.common.adapter.TabPagerAdapter
import com.taufik.themovieshow.utils.extensions.createCustomTabView
import com.taufik.themovieshow.utils.extensions.setupTabLayoutBinding
import com.taufik.themovieshow.utils.extensions.showSuccessToasty
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private var doubleBackToExitPressedOnce = false

    private val backPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (doubleBackToExitPressedOnce) {
                requireActivity().finish()
                return
            }

            doubleBackToExitPressedOnce = true
            requireContext().showSuccessToasty(getString(R.string.tvPressBackExit))

            lifecycleScope.launch {
                delay(2.seconds)
                doubleBackToExitPressedOnce = false
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
        setToolbar()
        setTabLayout()
        setActionClick()
    }

    private fun setToolbar() {
        binding.toolbarMovie.tvToolbar.text = getString(R.string.icMovies)
    }

    private fun setTabLayout() {
        binding.apply {
            setupTabLayoutBinding(
                tabLayout = tabLayoutMovie,
                viewPager = viewPagerMovie,
                fragments = listOf(
                    MovieTrendingFragment(),
                    MovieNowPlayingFragment(),
                    MovieUpcomingFragment()
                ),
                tabTitles = tabsTitle
            )
        }
    }

    private fun setActionClick() {
        binding.fabMovie.setOnClickListener {
            findNavController().navigate(R.id.discoverMovieFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @StringRes
        private val tabsTitle = intArrayOf(
            R.string.tvTrending,
            R.string.tvMovieNowPlaying,
            R.string.tvMovieUpcoming
        )
    }
}