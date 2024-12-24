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
import com.taufik.themovieshow.ui.tvshow.adapter.TvShowsTrendingAdapter
import com.taufik.themovieshow.ui.tvshow.viewmodel.DetailTvShowViewModel
import com.taufik.themovieshow.ui.tvshow.viewmodel.TvShowsViewModel
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.filterAndSortByDate
import com.taufik.themovieshow.utils.hideLoading
import com.taufik.themovieshow.utils.navigateToDetailTvShow
import com.taufik.themovieshow.utils.showLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowTrendingFragment : Fragment() {

    private var _binding: FragmentMovieTvShowsListBinding? = null
    private val binding get() = _binding

    private val viewModel by viewModels<TvShowsViewModel>()
    private val detailTvShowViewModel by viewModels<DetailTvShowViewModel>()
    private var tvShowsTrendingAdapter: TvShowsTrendingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieTvShowsListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding?.rvCommon?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowsTrendingAdapter
        }
    }

    private fun setData() {
        binding?.apply {
            viewModel.getTvShowsTrending().observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Loading -> pbLoading.showLoading()
                    is NetworkResult.Success -> {
                        pbLoading.hideLoading()
                        val filteredAndSortedTvShows = it.data?.results?.filterAndSortByDate(
                            getDate = { tvShow -> tvShow.firstAirDate },
                            inputFormat = CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                            thresholdFormat = CommonDateFormatConstants.DD_MM_YYYY_FORMAT
                        )
                        tvShowsTrendingAdapter?.submitList(filteredAndSortedTvShows)
                    }

                    is NetworkResult.Error -> {
                        pbLoading.hideLoading()
                        showError(it.message)
                    }
                }
            }
        }
    }

    private fun showError(message: String?) {
        binding?.layoutError?.apply {
            root.isVisible = true
            tvErrorDesc.text = message
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tvShowsTrendingAdapter = null
    }
}