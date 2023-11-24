package com.taufik.themovieshow.ui.tvshow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.databinding.FragmentMovieTvShowsListBinding
import com.taufik.themovieshow.ui.favorite.viewmodel.FavoriteTvShowViewModel
import com.taufik.themovieshow.ui.tvshow.adapter.TvShowsAdapter
import com.taufik.themovieshow.ui.tvshow.viewmodel.TvShowsViewModel
import com.taufik.themovieshow.utils.navigateToDetailTvShow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowAiringTodayFragment : Fragment() {

    private var _binding: FragmentMovieTvShowsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TvShowsViewModel>()
    private var tvShowsAdapter: TvShowsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieTvShowsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setData()
    }

    private fun setAdapter() {
        tvShowsAdapter = TvShowsAdapter {
            navigateToDetailTvShow(it.id, it.name, FavoriteTvShowViewModel.position)
        }

        binding.rvCommon.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowsAdapter
        }
    }

    private fun setData() {
        binding.apply {
            viewModel.getTvShowsAiringToday().observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Loading -> showLoading(true)
                    is NetworkResult.Success -> {
                        showLoading(false)
                        tvShowsAdapter?.submitList(it.data?.results)
                    }
                    is NetworkResult.Error -> {
                        showLoading(false)
                        showError(it.message)
                    }
                    else -> {
                        showLoading(false)
                        showError(it.message)
                    }
                }
            }
        }
    }

    private fun showLoading(isShow: Boolean) {
        binding.pbLoading.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun showError(message: String?) {
        binding.layoutError.apply {
            root.isVisible = true
            tvErrorDesc.text = message
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tvShowsAdapter = null
    }
}