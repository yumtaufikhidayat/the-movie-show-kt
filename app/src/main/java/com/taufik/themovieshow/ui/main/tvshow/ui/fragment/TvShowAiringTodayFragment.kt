package com.taufik.themovieshow.ui.main.tvshow.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.databinding.FragmentTvShowAiringTodayBinding
import com.taufik.themovieshow.ui.main.tvshow.ui.adapter.TvShowsAdapter
import com.taufik.themovieshow.ui.main.tvshow.viewmodel.TvShowsViewModel

class TvShowAiringTodayFragment : Fragment() {

    private lateinit var tvShowsAiringTodayBinding: FragmentTvShowAiringTodayBinding
    private lateinit var viewModel: TvShowsViewModel
    private lateinit var tvShowsAdapter: TvShowsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        tvShowsAiringTodayBinding = FragmentTvShowAiringTodayBinding.inflate(inflater, container, false)
        return tvShowsAiringTodayBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

        setViewModel()

        setRecyclerView()

        setData()
    }

    private fun setAdapter() {
        tvShowsAdapter = TvShowsAdapter()
        tvShowsAdapter.notifyDataSetChanged()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[TvShowsViewModel::class.java]
    }

    private fun setRecyclerView() {
        with(tvShowsAiringTodayBinding.rvTvShow) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowsAdapter
        }
    }

    private fun setData() {

        showLoading(true)

        viewModel.setTvShowsAiringToday(BuildConfig.API_KEY)
        viewModel.getTvShowsAiringToday().observe(viewLifecycleOwner, {
            if (it != null) {
                tvShowsAdapter.setTvShows(it)
                showLoading(false)
            }
        })

        showLoading(false)
    }

    private fun showLoading(state: Boolean) {

        tvShowsAiringTodayBinding.apply {
            if (state) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }
}