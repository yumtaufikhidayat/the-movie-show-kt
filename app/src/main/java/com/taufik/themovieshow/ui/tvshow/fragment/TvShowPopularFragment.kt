package com.taufik.themovieshow.ui.tvshow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.base.fragment.BaseFragment
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.databinding.FragmentMovieTvShowsListBinding
import com.taufik.themovieshow.ui.detail.movie_tvshow.viewmodel.DetailMovieTvShowViewModel
import com.taufik.themovieshow.ui.tvshow.adapter.TvShowsAdapter
import com.taufik.themovieshow.ui.tvshow.viewmodel.TvShowsViewModel
import com.taufik.themovieshow.utils.enums.FROM
import com.taufik.themovieshow.utils.objects.CommonDateFormatConstants
import com.taufik.themovieshow.utils.extensions.filterAndSortByDate
import com.taufik.themovieshow.utils.extensions.hideView
import com.taufik.themovieshow.utils.extensions.navigateToDetailMovieTvShow
import com.taufik.themovieshow.utils.extensions.showError
import com.taufik.themovieshow.utils.extensions.showView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowPopularFragment : BaseFragment<FragmentMovieTvShowsListBinding>() {

    private val viewModel by viewModels<TvShowsViewModel>()
    private val detailMovieTvShowViewModel by viewModels<DetailMovieTvShowViewModel>()
    private var tvShowsAdapter: TvShowsAdapter? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieTvShowsListBinding = FragmentMovieTvShowsListBinding.inflate(inflater, container, false)

    override fun onFragmentReady(savedInstanceState: Bundle?) {
        setAdapter()
        setData()
    }

    private fun setAdapter() {
        tvShowsAdapter = TvShowsAdapter {
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
            adapter = tvShowsAdapter
        }
    }

    private fun setData() {
        binding.apply {
            viewModel.getTvShowsPopular().observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Loading -> pbLoading.showView()
                    is NetworkResult.Success -> {
                        pbLoading.hideView()
                        val filteredAndSortedTvShows = it.data?.results?.filterAndSortByDate(
                            getDate = { tvShow -> tvShow.firstAirDate },
                            inputFormat = CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                            thresholdFormat = CommonDateFormatConstants.DD_MM_YYYY_FORMAT
                        )
                        tvShowsAdapter?.submitList(filteredAndSortedTvShows)
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
        tvShowsAdapter = null
    }
}