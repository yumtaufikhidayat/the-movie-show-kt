package com.taufik.themovieshow.ui.main.movie.fragment

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
import com.taufik.themovieshow.ui.detail.movie.fragment.DetailMovieFragment
import com.taufik.themovieshow.ui.main.LoadMoreAdapter
import com.taufik.themovieshow.ui.main.movie.adapter.MovieAdapter
import com.taufik.themovieshow.ui.main.movie.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieNowPlayingFragment : Fragment() {

    private var _binding: FragmentMovieTvShowsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MovieViewModel>()
    private var movieAdapter: MovieAdapter? = null

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
        binding.rvCommon.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = movieAdapter
        }
    }

    private fun setData() {
        binding.apply {
            lifecycleScope.launch {
                movieAdapter = MovieAdapter {
                    val bundle = Bundle().apply {
                        putInt(DetailMovieFragment.EXTRA_ID, it.id)
                        putString(DetailMovieFragment.EXTRA_TITLE, it.title)
                    }
                    findNavController().navigate(R.id.detailMovieFragment, bundle)
                }

                viewModel.setMovieNowPlaying().observe(viewLifecycleOwner) {
                    movieAdapter?.submitData(viewLifecycleOwner.lifecycle, it)
                }

                movieAdapter?.apply {
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
                            movieAdapter?.retry()
                        }
                    }
                }
            }

            rvCommon.adapter = movieAdapter?.withLoadStateHeaderAndFooter(
                header = LoadMoreAdapter { movieAdapter?.retry() },
                footer = LoadMoreAdapter { movieAdapter?.retry() }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        movieAdapter = null
    }
}