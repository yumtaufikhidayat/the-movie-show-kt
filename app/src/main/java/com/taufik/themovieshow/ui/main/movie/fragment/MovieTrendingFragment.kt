package com.taufik.themovieshow.ui.main.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.data.viewmodel.movie.MovieViewModel
import com.taufik.themovieshow.databinding.FragmentMovieTrendingBinding
import com.taufik.themovieshow.ui.main.movie.adapter.MovieTrendingAdapter

class MovieTrendingFragment : Fragment() {

    private var _binding: FragmentMovieTrendingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModels()
    private val movieTrendingAdapter by lazy { MovieTrendingAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieTrendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setData()
    }

    private fun setAdapter() = with(binding) {
        rvTrendingMovie.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = movieTrendingAdapter
        }
    }

    private fun setData() {
        showLoading(true)
        viewModel.apply {
            setMovieTrendingDay()
            listTrendingDay.observe(viewLifecycleOwner) {
                if (it != null) {
                    movieTrendingAdapter.submitList(it)
                    showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isShow: Boolean) = with(binding){
        progressBar.isVisible = isShow
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}