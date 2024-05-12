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
import com.taufik.themovieshow.utils.navigateToDetailTvShow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowTrendingFragment : Fragment() {

    private var _binding: FragmentMovieTvShowsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TvShowsViewModel>()
    private val detailTvShowViewModel by viewModels<DetailTvShowViewModel>()
    private var tvShowsTrendingAdapter: TvShowsTrendingAdapter? = null

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
        viewModel.getTvShowsTrending().observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Loading -> showLoading(true)
                is NetworkResult.Success -> {
                    showLoading(false)
                    tvShowsTrendingAdapter?.submitList(it.data?.results)
                }

                is NetworkResult.Error -> {
                    showLoading(false)
                    showError(it.message)
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
        tvShowsTrendingAdapter = null
    }
}