package com.taufik.themovieshow.ui.tvshow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.databinding.FragmentTvShowTrendingBinding
import com.taufik.themovieshow.ui.tvshow.adapter.TvShowsTrendingAdapter
import com.taufik.themovieshow.ui.tvshow.viewmodel.TvShowsViewModel

class TvShowTrendingFragment : Fragment() {

    private lateinit var tvShowsTrendingBinding: FragmentTvShowTrendingBinding
    private lateinit var viewModel: TvShowsViewModel
    private lateinit var tvShowsTrendingAdapter: TvShowsTrendingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        tvShowsTrendingBinding = FragmentTvShowTrendingBinding.inflate(inflater, container, false)
        return tvShowsTrendingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

        setViewModel()

        setRecyclerView()

        setData()
    }

    private fun setAdapter() {
        tvShowsTrendingAdapter = TvShowsTrendingAdapter()
        tvShowsTrendingAdapter.notifyDataSetChanged()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[TvShowsViewModel::class.java]
    }

    private fun setRecyclerView() {
        with(tvShowsTrendingBinding.rvTrendingMovie) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowsTrendingAdapter
        }
    }

    private fun setData() {

        showLoading(true)

        viewModel.setTvShowsTrending(BuildConfig.API_KEY)
        viewModel.getTvShowsTrending().observe(viewLifecycleOwner, {
            if (it != null) {
                tvShowsTrendingAdapter.setTvShows(it)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {

        tvShowsTrendingBinding.apply {
            if (state) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }
}