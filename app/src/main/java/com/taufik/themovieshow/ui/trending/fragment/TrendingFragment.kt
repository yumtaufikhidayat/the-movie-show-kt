package com.taufik.themovieshow.ui.trending.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentTrendingBinding
import com.taufik.themovieshow.ui.trending.adapter.TrendingPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class TrendingFragment : Fragment() {

    private var _binding: FragmentTrendingBinding? = null
    private val binding get() = _binding!!

    private var doubleBackToExitPressedOnce = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrendingBinding.inflate(inflater, container, false)
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
    }

    private fun setToolbar() {
        binding.toolbarTrending.tvToolbar.text = getString(R.string.icTrending)
    }

    private fun setTabLayout() {
        binding.apply {
            val mainPagerAdapter = TrendingPagerAdapter(this@TrendingFragment)
            viewPagerTrending.adapter = mainPagerAdapter
            TabLayoutMediator(tabLayoutTrending, viewPagerTrending) { tabs, position ->
                tabs.text = getString(tabsTitle[position])
            }.attach()
        }
    }

    private val backPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (doubleBackToExitPressedOnce) {
                requireActivity().finish()
                return
            }

            doubleBackToExitPressedOnce = true
            Toasty.info(requireContext(), "Press back again to exit", Toast.LENGTH_SHORT).show()

            lifecycleScope.launch {
                delay(2.seconds)
                doubleBackToExitPressedOnce = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @StringRes
        private val tabsTitle = intArrayOf(R.string.tvMovies, R.string.tvShows)
    }
}