package com.taufik.themovieshow.ui.main.tvshow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentMovieTvShowsListBinding
import com.taufik.themovieshow.ui.detail.tvshow.fragment.DetailTvShowFragment
import com.taufik.themovieshow.ui.main.LoadMoreAdapter
import com.taufik.themovieshow.ui.main.tvshow.adapter.TvShowsTrendingAdapter
import com.taufik.themovieshow.ui.main.tvshow.viewmodel.TvShowsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TvShowTrendingFragment : Fragment() {

    private var _binding: FragmentMovieTvShowsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TvShowsViewModel>()
    private var tvShowsTrendingAdapter: TvShowsTrendingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieTvShowsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setData()
    }

    private fun setAdapter() {
        binding.rvCommon.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowsTrendingAdapter
        }
    }

    private fun setData() {
        binding.apply {
            tvShowsTrendingAdapter = TvShowsTrendingAdapter {
                val bundle = Bundle().apply {
                    putInt(DetailTvShowFragment.EXTRA_ID, it.id)
                    putString(DetailTvShowFragment.EXTRA_TITLE, it.name)
                }
                findNavController().navigate(R.id.detailTvShowFragment, bundle)
            }

            lifecycleScope.launch {
                viewModel.setTvShowsTrending().collect {
                    tvShowsTrendingAdapter?.submitData(viewLifecycleOwner.lifecycle, it)
                }
            }

            tvShowsTrendingAdapter?.apply {
                layoutError.apply {
                    addLoadStateListener { loadState ->
                        val loadStateRefresh = loadState.source.refresh
                        pbLoading.isVisible = loadStateRefresh is LoadState.Loading
                        rvCommon.isVisible = loadStateRefresh is LoadState.NotLoading
                        tvErrorTitle.apply {
                            isVisible = loadStateRefresh is LoadState.Error
                            text = getString(R.string.tvOops)
                            setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.colorOrange
                                )
                            )
                        }
                        tvError.apply {
                            isVisible = loadStateRefresh is LoadState.Error
                            text = getString(R.string.tvUnableLoadData)
                            setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.colorOrange
                                )
                            )
                        }
                        btnRetry.isVisible = loadStateRefresh is LoadState.Error

                        if (loadStateRefresh is LoadState.NotLoading
                            && loadState.append.endOfPaginationReached
                            && itemCount < 1
                        ) {
                            rvCommon.isVisible = false
                            tvEmptyTitle.isVisible = true
                            tvEmpty.isVisible = true
                        } else {
                            tvEmptyTitle.isVisible = false
                            tvEmpty.isVisible = false
                        }
                    }

                    btnRetry.setOnClickListener {
                        tvShowsTrendingAdapter?.retry()
                    }
                }
            }

            rvCommon.adapter = tvShowsTrendingAdapter?.withLoadStateHeaderAndFooter(
                header = LoadMoreAdapter { tvShowsTrendingAdapter?.retry() },
                footer = LoadMoreAdapter { tvShowsTrendingAdapter?.retry() }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tvShowsTrendingAdapter = null
    }
}