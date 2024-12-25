package com.taufik.themovieshow.ui.discover

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.databinding.FragmentDiscoverMovieBinding
import com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResult
import com.taufik.themovieshow.ui.movie.adapter.DiscoverMovieAdapter
import com.taufik.themovieshow.ui.movie.viewmodel.DetailMovieViewModel
import com.taufik.themovieshow.ui.movie.viewmodel.MovieViewModel
import com.taufik.themovieshow.utils.extensions.navigateToDetailMovie
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoverMovieFragment : Fragment() {

    private var _binding: FragmentDiscoverMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MovieViewModel>()
    private val detailMovieViewModel by viewModels<DetailMovieViewModel>()
    private var discoverMovieAdapter: DiscoverMovieAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDiscoverMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initAdapter()
        initSearch()
    }

    private fun initToolbar() {
        binding.toolbarSearchMovie.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter() {
        discoverMovieAdapter = DiscoverMovieAdapter {
            detailMovieViewModel.apply {
                idMovie = it.id
                titleMovie = it.title
            }
            navigateToDetailMovie(detailMovieViewModel.idMovie, detailMovieViewModel.titleMovie)
        }

        binding.rvDiscoverMovie.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = discoverMovieAdapter
        }
    }

    private fun initSearch() {
        showNoSearch(
            title = getString(R.string.tvNoSearchMovie),
            description = getString(R.string.tvSearchMovie)
        )

        binding.toolbarSearchMovie.etSearch.apply {
            showKeyboard()
            setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard()
                    return@OnEditorActionListener true
                }
                false
            })

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // no action before text changed
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    showNoResults(true, emptyList(), p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {
                    showSearchData(p0)
                }
            })
        }
    }

    private fun showSearchData(p0: Editable?) {
        viewModel.apply {
            val query = p0.toString()
            setDiscoverMovie(query)
            discoverMovieResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> showNoResults(false, emptyList(), query)
                    is NetworkResult.Success -> {
                        val data = response.data
                        val results = data?.results
                        if (results != null) {
                            when {
                                results.isEmpty() -> showNoResults(true, results, query)
                                query.isNotEmpty() -> {
                                    discoverMovieAdapter?.submitList(results)
                                    showNoResults(false, results, query)
                                }
                                else -> showNoResults(true, emptyList(), query)
                            }
                        } else {
                            showNoResults(true, emptyList(), query)
                        }
                    }
                    is NetworkResult.Error -> showNoResults(false, emptyList(), query)
                }
            }
        }
    }

    private fun showKeyboard() {
        binding.toolbarSearchMovie.apply {
            etSearch.requestFocus()
            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyboard() {
        binding.apply {
            toolbarSearchMovie.apply {
                etSearch.clearFocus()
                val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
            }
        }
    }

    private fun showNoResults(isShow: Boolean, list: List<DiscoverMovieResult>?, query: String) {
        var textSearchTitle = ""
        var textSearch = ""

        if (isShow) {
            when {
                query.isEmpty() || query.isBlank() -> {
                    textSearchTitle = getString(R.string.tvNoSearchMovie)
                    textSearch = getString(R.string.tvSearchMovie)
                }
                list?.isEmpty() == true -> {
                    textSearchTitle = getString(R.string.tvOopsNoResults)
                    textSearch = getString(R.string.tvNoSearchData)
                }
            }

            discoverMovieAdapter?.submitList(list)
            showNoSearch(
                title = textSearchTitle,
                description = textSearch
            )
        } else {
            binding.layoutNoSearch.root.isVisible = false
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}