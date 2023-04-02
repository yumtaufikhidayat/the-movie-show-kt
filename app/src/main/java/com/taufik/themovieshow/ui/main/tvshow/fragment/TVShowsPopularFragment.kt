package com.taufik.themovieshow.ui.main.tvshow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.databinding.FragmentTvShowsPopularBinding
import com.taufik.themovieshow.ui.main.tvshow.adapter.TvShowsAdapter
import com.taufik.themovieshow.ui.main.tvshow.viewmodel.TvShowsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TVShowsPopularFragment : Fragment() {

    private var _binding: FragmentTvShowsPopularBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TvShowsViewModel>()
    private val tvShowsAdapter by lazy { TvShowsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTvShowsPopularBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setData()
    }

    private fun setAdapter()  {
        binding.rvTvShow.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowsAdapter
        }
    }

    private fun setData() {
        viewModel.apply {
            setTvShowsPopular()
                tvShowPopularResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> showLoading(true)
                    is NetworkResult.Success -> {
                        showLoading(false)
                        val data = response.data
                        if (data != null) tvShowsAdapter.submitList(data.results)
                    }
                    is NetworkResult.Error -> showLoading(false)
                }
            }
        }
    }

    private fun showLoading(isShow: Boolean) =binding.progressBar.isVisible == isShow

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}