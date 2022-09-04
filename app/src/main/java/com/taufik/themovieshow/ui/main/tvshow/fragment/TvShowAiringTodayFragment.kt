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
import com.taufik.themovieshow.databinding.FragmentTvShowAiringTodayBinding
import com.taufik.themovieshow.ui.main.tvshow.adapter.TvShowsAdapter

class TvShowAiringTodayFragment : Fragment() {

    private var _binding: FragmentTvShowAiringTodayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TvShowsViewModel by viewModels()
    private lateinit var tvShowsAdapter: TvShowsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTvShowAiringTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        setData()
    }

    private fun setAdapter() = with(binding) {
        tvShowsAdapter = TvShowsAdapter()
        rvTvShow.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowsAdapter
        }
    }

    private fun setData() {
        showLoading(true)
        viewModel.apply {
            setTvShowsAiringToday(BuildConfig.API_KEY)
            listAiringToday.observe(viewLifecycleOwner) {
                if (it != null) {
                    tvShowsAdapter.submitList(it)
                    showLoading(false)
                }
            }
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