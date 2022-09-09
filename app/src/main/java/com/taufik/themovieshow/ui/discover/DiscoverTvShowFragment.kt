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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.data.viewmodel.tvshow.TvShowsViewModel
import com.taufik.themovieshow.databinding.FragmentDiscoverTvShowBinding
import com.taufik.themovieshow.ui.main.tvshow.adapter.DiscoverTvShowsAdapter

class DiscoverTvShowFragment : Fragment() {

    private var _binding: FragmentDiscoverTvShowBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TvShowsViewModel>()
    private val discoverTvShowsAdapter by lazy { DiscoverTvShowsAdapter() }

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

    private fun initToolbar() = with(binding) {
        toolbarSearchTvShow.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter() = with(binding) {
        rvSearchTvShow.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = discoverTvShowsAdapter
        }
    }

    private fun initSearch() = with(binding) {
        toolbarSearchTvShow.etSearch.apply {
            setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard()
                    return@OnEditorActionListener true
                }
                false
            })

            addTextChangedListener(textWatcher())
        }
    }

    private fun showSearchData(query: Editable) = with(binding) {
        showLoading(true)
        viewModel.apply {
            setDiscoverTvShows(query.toString())
            listDiscover.observe(viewLifecycleOwner) {
                if (it != null) {
                    discoverTvShowsAdapter.submitList(it)
                    showLoading(false)
                }
            }
        }
    }

    private fun textWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable) {
            showSearchData(p0)
        }
    }

    private fun showLoading(isShow: Boolean) = with(binding) {
        progressBar.isVisible = isShow
    }

    private fun hideKeyboard() = with(binding) {
        toolbarSearchTvShow.apply {
            etSearch.clearFocus()
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}