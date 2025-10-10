package com.taufik.themovieshow.ui.tvshow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.base.fragment.BaseFragment
import com.taufik.themovieshow.databinding.FragmentMovieTvShowsListBinding
import com.taufik.themovieshow.ui.detail.movie_tvshow.viewmodel.DetailMovieTvShowViewModel
import com.taufik.themovieshow.ui.tvshow.adapter.TvShowsTrendingAdapter
import com.taufik.themovieshow.ui.tvshow.viewmodel.TvShowsViewModel
import com.taufik.themovieshow.utils.enums.FROM
import com.taufik.themovieshow.utils.extensions.filterAndSortByDate
import com.taufik.themovieshow.utils.extensions.hideView
import com.taufik.themovieshow.utils.extensions.navigateToDetailMovieTvShow
import com.taufik.themovieshow.utils.extensions.observeNetworkResult
import com.taufik.themovieshow.utils.extensions.showError
import com.taufik.themovieshow.utils.extensions.showView
import com.taufik.themovieshow.utils.objects.CommonDateFormatConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowTrendingFragment : BaseFragment<FragmentMovieTvShowsListBinding>() {

    private val viewModel by viewModels<TvShowsViewModel>()
    private val detailMovieTvShowViewModel by viewModels<DetailMovieTvShowViewModel>()
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
            detailMovieTvShowViewModel.apply {
                idMovieTvShow = it.id
                titleMovieTvShow = it.name
            }
            navigateToDetailMovieTvShow(
                detailMovieTvShowViewModel.idMovieTvShow,
                detailMovieTvShowViewModel.titleMovieTvShow,
                FROM.TV_SHOW
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
            viewModel.getTvShowsTrending().observeNetworkResult(
                lifecycleOwner = viewLifecycleOwner,
                onLoading = {
                    pbLoading.showView()
                },
                onSuccess = { response ->
                    pbLoading.hideView()
                    val filteredAndSortedTvShows = response.results.filterAndSortByDate(
                        getDate = { tvShow -> tvShow.firstAirDate },
                        inputFormat = CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                        thresholdFormat = CommonDateFormatConstants.DD_MM_YYYY_FORMAT
                    )
                    tvShowsTrendingAdapter?.submitList(filteredAndSortedTvShows)
                },
                onError = { message ->
                    pbLoading.hideView()
                    layoutError.showError(message)
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tvShowsTrendingAdapter = null
    }
}