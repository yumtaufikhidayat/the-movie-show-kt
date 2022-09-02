package com.taufik.themovieshow.ui.main.tvshow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.data.viewmodel.tvshow.TvShowsViewModel
import com.taufik.themovieshow.databinding.FragmentTvShowTrendingBinding
import com.taufik.themovieshow.ui.main.tvshow.adapter.TvShowsTrendingAdapter

class TvShowTrendingFragment : Fragment() {

    private var _binding: FragmentTvShowTrendingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TvShowsViewModel by viewModels()

    private lateinit var tvShowsTrendingAdapter: TvShowsTrendingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTvShowTrendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setData()
    }

    private fun setData() {
        showLoading(true)
        viewModel.setTvShowsTrending()
        viewModel.getTvShowsTrending().observe(viewLifecycleOwner) {
            if (it != null) {
                tvShowsTrendingAdapter.submitList(it)
                showLoading(false)
            }
        }
    }

    private fun setAdapter() = with(binding) {
        tvShowsTrendingAdapter = TvShowsTrendingAdapter()
        rvTrendingMovie.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowsTrendingAdapter
        }
    }

    private fun showLoading(isShow: Boolean) = with(binding) {
        progressBar.isVisible = isShow
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}