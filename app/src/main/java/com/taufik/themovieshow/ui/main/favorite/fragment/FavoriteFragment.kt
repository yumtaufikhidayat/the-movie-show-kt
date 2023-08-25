package com.taufik.themovieshow.ui.main.favorite.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentFavoriteBinding
import com.taufik.themovieshow.ui.main.favorite.adapter.FavoritePagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setTabLayout()
    }

    private fun setToolbar() {
        binding.toolbarFavorite.tvToolbar.text = getString(R.string.icFavorite)
    }

    private fun setTabLayout() {
        binding.apply {
            val discoverPagerAdapter = FavoritePagerAdapter(this@FavoriteFragment)
            viewPagerFavorite.adapter = discoverPagerAdapter
            TabLayoutMediator(tabLayoutFavorite, viewPagerFavorite) { tabs, position ->
                tabs.text = getString(tabsTitle[position])
            }.attach()
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