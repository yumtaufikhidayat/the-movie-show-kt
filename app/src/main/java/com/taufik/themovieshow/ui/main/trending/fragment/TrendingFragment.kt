package com.taufik.themovieshow.ui.main.trending.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentTrendingBinding
import com.taufik.themovieshow.ui.main.trending.adapter.TrendingPagerAdapter
import es.dmoral.toasty.Toasty


class TrendingFragment : Fragment() {

    private var _binding: FragmentTrendingBinding? = null
    private val binding get() = _binding!!

    private val delayTime = 2000L
    private var doubleBackToExitPressedOnce = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
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

    private fun setTabLayout() = with(binding) {
        val mainPagerAdapter = TrendingPagerAdapter(this@TrendingFragment)
        viewPagerTrending.adapter = mainPagerAdapter
        TabLayoutMediator(tabLayoutTrending, viewPagerTrending) { tabs, position ->
            tabs.text = getString(tabsTitle[position])
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val backPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (doubleBackToExitPressedOnce) {
                requireActivity().finish()
                return
            }

            doubleBackToExitPressedOnce = true
            Toasty.info(requireContext(), "Press back again to exit", Toast.LENGTH_SHORT).show()

            Handler(Looper.getMainLooper())
                .postDelayed({
                    doubleBackToExitPressedOnce = false
                }, delayTime)
        }
    }

    companion object {
        @StringRes
        private val tabsTitle = intArrayOf(R.string.tvMovies, R.string.tvShows)
    }
}