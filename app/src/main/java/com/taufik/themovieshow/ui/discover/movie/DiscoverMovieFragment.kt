package com.taufik.themovieshow.ui.discover.movie

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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.data.viewmodel.movie.MovieViewModel
import com.taufik.themovieshow.databinding.FragmentDiscoverMovieBinding
import com.taufik.themovieshow.ui.main.movie.adapter.DiscoverMovieAdapter

class DiscoverMovieFragment : Fragment() {

    private var _binding: FragmentDiscoverMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModels()

    private lateinit var discoverMovieAdapter: DiscoverMovieAdapter

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
        discoverMovieAdapter = DiscoverMovieAdapter()
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

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable) {
                    showSearchData(p0)
                }
            })
        }
    }

    private fun showSearchData(query: Editable) = with(binding) {
        showLoading(true)
        viewModel.apply {
            setDiscoverMovie(query.toString())
            getDiscoverMovie().observe(viewLifecycleOwner) {
                if (it != null) {
                    discoverMovieAdapter.submitList(it)
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isShow: Boolean) = with(binding) {
        progressBar.isVisible = isShow
    }

    private fun hideKeyboard() = with(binding) {
        toolbarSearchMovie.apply {
            etSearch.clearFocus()
            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etSearch.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}