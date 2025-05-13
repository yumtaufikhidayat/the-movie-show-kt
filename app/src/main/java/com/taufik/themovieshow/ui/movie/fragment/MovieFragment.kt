package com.taufik.themovieshow.ui.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentMovieBinding
import com.taufik.themovieshow.ui.tablayout.MovieTvShowTabViewModel
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
    private val movieTvShowTabViewModel: MovieTvShowTabViewModel by viewModels()

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
                tabTitles = tabsTitle,
                selectedIndex = movieTvShowTabViewModel.selectedTabIndex,
                onTabChanged = { position ->
                    movieTvShowTabViewModel.selectedTabIndex = position
                }
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