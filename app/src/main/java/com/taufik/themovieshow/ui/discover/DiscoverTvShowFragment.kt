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
import com.taufik.themovieshow.databinding.FragmentDiscoverTvShowBinding
import com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResult
import com.taufik.themovieshow.ui.detail.movie_tvshow.viewmodel.DetailMovieTvShowViewModel
import com.taufik.themovieshow.ui.tvshow.adapter.DiscoverTvShowsAdapter
import com.taufik.themovieshow.utils.enums.FROM
import com.taufik.themovieshow.utils.extensions.navigateToDetailMovieTvShow
import com.taufik.themovieshow.utils.extensions.showError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiscoverTvShowFragment : BaseFragment<FragmentDiscoverTvShowBinding>() {

    private val viewModel by viewModels<DiscoverTvShowViewModel>()
    private val detailMovieTvShowViewModel by viewModels<DetailMovieTvShowViewModel>()
    private var discoverTvShowsAdapter: DiscoverTvShowsAdapter? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDiscoverTvShowBinding = FragmentDiscoverTvShowBinding.inflate(inflater, container, false)

    override fun onFragmentReady(savedInstanceState: Bundle?) {
        initToolbar()
        initAdapter()
        initSearch()
        observeSearch()
    }

    private fun initToolbar() {
        binding.toolbarSearchTvShow.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter() {
        discoverTvShowsAdapter = DiscoverTvShowsAdapter {
            detailMovieTvShowViewModel.apply {
                idMovieTvShow = it.id
                titleMovieTvShow = it.name.orEmpty()
            }
            navigateToDetailMovieTvShow(
                detailMovieTvShowViewModel.idMovieTvShow,
                detailMovieTvShowViewModel.titleMovieTvShow,
                FROM.TV_SHOW
            )
        }

        binding.rvDiscoverTvShow.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = discoverTvShowsAdapter
        }
    }

    private fun initSearch() {
        binding.toolbarSearchTvShow.etSearch.apply {
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

    private fun render(state: SearchUiState<DiscoverTvShowsResult>) {
        binding.apply {
            pbLoading.isVisible = state is SearchUiState.Loading
            rvDiscoverTvShow.isVisible = state !is SearchUiState.Error
            layoutError.root.isVisible = state is SearchUiState.Error
            layoutNoSearch.root.isVisible = state is SearchUiState.Idle || state is SearchUiState.Empty

            when (state) {
                SearchUiState.Idle -> {
                    discoverTvShowsAdapter?.submitList(emptyList())
                    showNoSearch(
                        title = getString(R.string.tvNoSearchTvShow),
                        description = getString(R.string.tvSearchTvShow)
                    )
                }
                SearchUiState.Loading -> Unit
                is SearchUiState.Success -> discoverTvShowsAdapter?.submitList(state.items)
                SearchUiState.Empty -> {
                    discoverTvShowsAdapter?.submitList(emptyList())
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
        binding.toolbarSearchTvShow.apply {
            etSearch.requestFocus()
            WindowCompat.getInsetsController(requireActivity().window, etSearch)
                .show(WindowInsetsCompat.Type.ime())
        }
    }

    private fun hideKeyboard() {
        binding.apply {
            toolbarSearchTvShow.apply {
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
