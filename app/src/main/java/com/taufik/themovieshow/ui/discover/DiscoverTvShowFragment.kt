package com.taufik.themovieshow.ui.discover

import android.content.Context
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
import com.taufik.themovieshow.databinding.FragmentDiscoverTvShowBinding
import com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResult
import com.taufik.themovieshow.ui.main.tvshow.adapter.DiscoverTvShowsAdapter
import com.taufik.themovieshow.ui.main.tvshow.viewmodel.TvShowsViewModel
import com.taufik.themovieshow.utils.navigateToDetailTvShow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoverTvShowFragment : Fragment() {

    private var _binding: FragmentDiscoverTvShowBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TvShowsViewModel>()
    private var discoverTvShowsAdapter: DiscoverTvShowsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDiscoverTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initAdapter()
        initSearch()
    }

    private fun initToolbar() {
        binding.toolbarSearchTvShow.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter() {
        discoverTvShowsAdapter = DiscoverTvShowsAdapter {
            navigateToDetailTvShow(it.id, it.name)
        }

        binding.rvSearchTvShow.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = discoverTvShowsAdapter
        }
    }

    private fun initSearch() {
        binding.apply {
            toolbarSearchTvShow.etSearch.apply {
                showKeyboard()
                setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        hideKeyboard()
                        return@OnEditorActionListener true
                    }
                    false
                })

                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        showNoResults(true, emptyList(), p0.toString())
                    }

                    override fun afterTextChanged(p0: Editable?) {
                        showSearchData(p0)
                    }
                })
            }
        }
    }

    private fun showSearchData(p0: Editable?) {
        viewModel.apply {
            val query = p0.toString()
            setDiscoverTvShows(query)
            discoverTvShowsResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> showNoResults(false, emptyList(), query)
                    is NetworkResult.Success -> {
                        val data = response.data
                        val results = data?.results
                        if (results != null) {
                            when {
                                results.isEmpty() -> showNoResults(true, results, query)
                                query.isNotEmpty() -> {
                                    discoverTvShowsAdapter?.submitList(results)
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
        binding.toolbarSearchTvShow.apply {
            etSearch.requestFocus()
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyboard() {
        binding.apply {
            toolbarSearchTvShow.apply {
                etSearch.clearFocus()
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
            }
        }
    }

    private fun showNoResults(isShow: Boolean, list: List<DiscoverTvShowsResult>?, query: String) {
        binding.apply {
            var textSearchTitle = ""
            var textSearch = ""
            if (isShow) {
                when {
                    query.isEmpty() || query.isBlank() -> {
                        textSearchTitle = getString(R.string.tvNoSearchTvShow)
                        textSearch = getString(R.string.tvSearchTvShow)
                    }
                    list?.isEmpty() == true -> {
                        textSearchTitle = getString(R.string.tvOopsNoResults)
                        textSearch = getString(R.string.tvNoSearchData)
                    }
                }
                layoutNoSearch.apply {
                    root.isVisible = true
                    lottieEmptyBox.apply {
                        isVisible = true
                        setImageResource(R.drawable.ic_search_orange)
                    }
                    tvErrorTitle.apply {
                        isVisible = true
                        text = textSearchTitle
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOrange))
                    }
                    tvErrorDesc.apply {
                        isVisible = true
                        text = textSearch
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOrange))
                    }
                }
            } else {
                layoutNoSearch.root.isVisible = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}