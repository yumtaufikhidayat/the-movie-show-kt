package com.taufik.themovieshow.ui.discover

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.viewmodel.movie.MovieViewModel
import com.taufik.themovieshow.databinding.FragmentDiscoverMovieBinding
import com.taufik.themovieshow.ui.main.movie.adapter.DiscoverMovieAdapter

class DiscoverMovieFragment : Fragment() {

    private var _binding: FragmentDiscoverMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MovieViewModel>()
    private val discoverMovieAdapter by lazy { DiscoverMovieAdapter() }

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

    private fun initToolbar() = with(binding) {
        toolbarSearchMovie.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter() = with(binding) {
        rvSearchMovie.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = discoverMovieAdapter
        }
    }

    private fun initSearch() = with(binding) {
        toolbarSearchMovie.etSearch.apply {
            setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard()
                    return@OnEditorActionListener true
                }
                false
            })

            addTextChangedListener(afterTextChanged = { p0 ->
                showSearchData(p0)
            })
        }
    }

    private fun showSearchData(query: Editable?) {
        viewModel.apply {
            val q = query.toString()
            if (q.isNotEmpty()) {
                setDiscoverMovie(q)
                listDiscover.observe(viewLifecycleOwner) {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            discoverMovieAdapter.submitList(it)
                            showNoResults(false)
                        } else {
                            showNoResults(true)
                        }
                    }
                }
            }
        }
    }

    private fun hideKeyboard() = with(binding) {
        toolbarSearchMovie.apply {
            etSearch.clearFocus()
            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
        }
    }

    private fun showNoResults(isShow: Boolean) = with(binding) {
        if (isShow) {
            layoutNoSearch.apply {
                root.isVisible = true
                imgError.setImageResource(R.drawable.ic_search_orange)
                tvError.text = getString(R.string.tvNoSearchData)
            }
        } else {
            layoutNoSearch.root.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}