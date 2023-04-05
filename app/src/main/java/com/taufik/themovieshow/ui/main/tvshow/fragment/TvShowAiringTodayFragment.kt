package com.taufik.themovieshow.ui.main.tvshow.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.FragmentTvShowAiringTodayBinding
import com.taufik.themovieshow.ui.detail.tvshow.fragment.DetailTvShowFragment
import com.taufik.themovieshow.ui.main.LoadMoreAdapter
import com.taufik.themovieshow.ui.main.tvshow.adapter.TvShowsAdapter
import com.taufik.themovieshow.ui.main.tvshow.viewmodel.TvShowsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TvShowAiringTodayFragment : Fragment() {

    private var _binding: FragmentTvShowAiringTodayBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TvShowsViewModel>()
    private var tvShowsAdapter: TvShowsAdapter? = null

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

    private fun setAdapter() {
        binding.rvTvShow.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowsAdapter
        }
    }

    private fun setData() {
        lifecycleScope.launch {
            tvShowsAdapter = TvShowsAdapter {
                val bundle = Bundle().apply {
                    putInt(DetailTvShowFragment.EXTRA_ID, it.id)
                    putString(DetailTvShowFragment.EXTRA_TITLE, it.name)
                }
                findNavController().navigate(R.id.detailTvShowFragment, bundle)
            }

            viewModel.setTvShowsAiringToday().collect {
                tvShowsAdapter?.submitData(it)
            }

            tvShowsAdapter?.loadStateFlow?.collect {
                showLoading(it.refresh is LoadState.Loading)
            }
        }

        binding.rvTvShow.adapter = tvShowsAdapter?.withLoadStateFooter( LoadMoreAdapter {
            tvShowsAdapter?.retry()
        })
    }

    private fun showLoading(isShow: Boolean) {
        binding.progressBar.isVisible = isShow
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tvShowsAdapter = null
    }
}