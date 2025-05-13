package com.taufik.themovieshow.ui.tvshow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentTvShowBinding
import com.taufik.themovieshow.ui.tablayout.MovieTvShowTabViewModel
import com.taufik.themovieshow.utils.extensions.setupTabLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class TvShowFragment : Fragment() {

    private var _binding: FragmentTvShowBinding? = null
    private val binding get() = _binding!!
    private val movieTvShowTabViewModel: MovieTvShowTabViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setTabLayout()
        setActionClick()
    }

    private fun setToolbar() {
        binding.toolbarTvShow.tvToolbar.text = getString(R.string.icTVShows)
    }

    private fun setTabLayout() {
        binding.apply {
            setupTabLayoutBinding(
                tabLayout = tabLayoutTvShow,
                viewPager = viewPagerTvShow,
                fragments = listOf(
                    TvShowTrendingFragment(),
                    TvShowAiringTodayFragment(),
                    TVShowsPopularFragment()
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
        binding.fabTvShow.setOnClickListener {
            findNavController().navigate(R.id.discoverTvShowFragment)
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
            R.string.tvTvShowAiringToday,
            R.string.tvTvShowPopular
        )
    }
}