package com.taufik.themovieshow.ui.tvshow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.base.BaseFragment
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.databinding.FragmentMovieTvShowsListBinding
import com.taufik.themovieshow.ui.tvshow.adapter.TvShowsTrendingAdapter
import com.taufik.themovieshow.ui.tvshow.viewmodel.DetailTvShowViewModel
import com.taufik.themovieshow.ui.tvshow.viewmodel.TvShowsViewModel
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.extensions.filterAndSortByDate
import com.taufik.themovieshow.utils.extensions.hideView
import com.taufik.themovieshow.utils.extensions.navigateToDetailTvShow
import com.taufik.themovieshow.utils.extensions.showError
import com.taufik.themovieshow.utils.extensions.showView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowTrendingFragment : BaseFragment<FragmentMovieTvShowsListBinding>() {

    private val viewModel by viewModels<TvShowsViewModel>()
    private val detailTvShowViewModel by viewModels<DetailTvShowViewModel>()
    private var tvShowsTrendingAdapter: TvShowsTrendingAdapter? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieTvShowsListBinding = FragmentMovieTvShowsListBinding.inflate(inflater, container, false)

    override fun onFragmentReady(savedInstanceState: Bundle?) {
        setAdapter()
        setData()
    }

    private fun setAdapter() {
        tvShowsTrendingAdapter = TvShowsTrendingAdapter {
            detailTvShowViewModel.apply {
                idTvShow = it.id
                titleTvShow = it.name
            }
            navigateToDetailTvShow(
                detailTvShowViewModel.idTvShow,
                detailTvShowViewModel.titleTvShow
            )
        }

        binding.rvCommon.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowsTrendingAdapter
        }
    }

    private fun setData() {
        binding.apply {
            viewModel.getTvShowsTrending().observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Loading -> pbLoading.showView()
                    is NetworkResult.Success -> {
                        pbLoading.hideView()
                        val filteredAndSortedTvShows = it.data?.results?.filterAndSortByDate(
                            getDate = { tvShow -> tvShow.firstAirDate },
                            inputFormat = CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                            thresholdFormat = CommonDateFormatConstants.DD_MM_YYYY_FORMAT
                        )
                        tvShowsTrendingAdapter?.submitList(filteredAndSortedTvShows)
                    }

                    is NetworkResult.Error -> {
                        pbLoading.hideView()
                        layoutError.showError(it.message)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tvShowsTrendingAdapter = null
    }
}