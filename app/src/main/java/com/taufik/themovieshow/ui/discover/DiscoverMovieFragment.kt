package com.taufik.themovieshow.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.R
import com.taufik.themovieshow.base.fragment.BaseFragment
import com.taufik.themovieshow.databinding.FragmentDiscoverMovieBinding
import com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResult
import com.taufik.themovieshow.ui.detail.movie_tvshow.viewmodel.DetailMovieTvShowViewModel
import com.taufik.themovieshow.ui.movie.adapter.DiscoverMovieAdapter
import com.taufik.themovieshow.utils.enums.FROM
import com.taufik.themovieshow.utils.extensions.navigateToDetailMovieTvShow
import com.taufik.themovieshow.utils.extensions.showError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiscoverMovieFragment : BaseFragment<FragmentDiscoverMovieBinding>() {

    private val viewModel by viewModels<DiscoverMovieViewModel>()
    private val detailMovieTvShowViewModel by viewModels<DetailMovieTvShowViewModel>()
    private var discoverMovieAdapter: DiscoverMovieAdapter? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDiscoverMovieBinding = FragmentDiscoverMovieBinding.inflate(inflater, container, false)

    override fun onFragmentReady(savedInstanceState: Bundle?) {
        initToolbar()
        initAdapter()
        initSearch()
        observeSearch()
    }

    private fun initToolbar() {
        binding.toolbarSearchMovie.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter() {
        discoverMovieAdapter = DiscoverMovieAdapter {
            detailMovieTvShowViewModel.apply {
                idMovieTvShow = it.id
                titleMovieTvShow = it.title.orEmpty()
            }
            navigateToDetailMovieTvShow(
                detailMovieTvShowViewModel.idMovieTvShow,
                detailMovieTvShowViewModel.titleMovieTvShow,
                FROM.MOVIE
            )
        }

        binding.rvDiscoverMovie.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = discoverMovieAdapter
        }
    }

    private fun initSearch() {
        binding.toolbarSearchMovie.etSearch.apply {
            showKeyboard()
            setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.searchNow()
                    hideKeyboard()
                    return@OnEditorActionListener true
                }
                false
            })

            doAfterTextChanged { viewModel.onQueryChanged(it.toString()) }
        }
    }

    private fun observeSearch() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchState.collect(::render)
            }
        }
    }

    private fun render(state: SearchUiState<DiscoverMovieResult>) {
        binding.apply {
            pbLoading.isVisible = state is SearchUiState.Loading
            rvDiscoverMovie.isVisible = state !is SearchUiState.Error
            layoutError.root.isVisible = state is SearchUiState.Error
            layoutNoSearch.root.isVisible = state is SearchUiState.Idle || state is SearchUiState.Empty

            when (state) {
                SearchUiState.Idle -> {
                    discoverMovieAdapter?.submitList(emptyList())
                    showNoSearch(
                        title = getString(R.string.tvNoSearchMovie),
                        description = getString(R.string.tvSearchMovie)
                    )
                }
                SearchUiState.Loading -> Unit
                is SearchUiState.Success -> discoverMovieAdapter?.submitList(state.items)
                SearchUiState.Empty -> {
                    discoverMovieAdapter?.submitList(emptyList())
                    showNoSearch(
                        title = getString(R.string.tvOopsNoResults),
                        description = getString(R.string.tvNoSearchData)
                    )
                }
                is SearchUiState.Error -> layoutError.showError(requireContext(), state.message) {
                    viewModel.searchNow()
                }
            }
        }
    }

    private fun showKeyboard() {
        binding.toolbarSearchMovie.apply {
            etSearch.requestFocus()
            WindowCompat.getInsetsController(requireActivity().window, etSearch)
                .show(WindowInsetsCompat.Type.ime())
        }
    }

    private fun hideKeyboard() {
        binding.apply {
            toolbarSearchMovie.apply {
                etSearch.clearFocus()
                WindowCompat.getInsetsController(requireActivity().window, etSearch)
                    .hide(WindowInsetsCompat.Type.ime())
            }
        }
    }

    private fun showNoSearch(title: String, description: String) {
        binding.layoutNoSearch.apply {
            root.isVisible = true
            imgNoSearch.apply {
                isVisible = true
                setImageResource(R.drawable.ic_search_orange)
            }
            tvErrorTitle.apply {
                isVisible = true
                text = title
                setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOrange))
            }
            tvErrorDesc.apply {
                isVisible = true
                text = description
                setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOrange))
            }
        }
    }
}
