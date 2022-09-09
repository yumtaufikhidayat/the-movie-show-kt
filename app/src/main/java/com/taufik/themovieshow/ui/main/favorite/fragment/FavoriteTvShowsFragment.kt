package com.taufik.themovieshow.ui.main.favorite.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.data.local.entity.FavoriteTvShow
import com.taufik.themovieshow.data.main.tvshow.popularairingtoday.TvShowsMainResult
import com.taufik.themovieshow.data.viewmodel.tvshow.FavoriteTvShowViewModel
import com.taufik.themovieshow.databinding.FragmentFavoriteTvShowsBinding
import com.taufik.themovieshow.ui.main.tvshow.adapter.TvShowsAdapter

class FavoriteTvShowsFragment : Fragment() {

    private var _binding: FragmentFavoriteTvShowsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteTvShowViewModel by viewModels()
    private val tvShowsAdapter by lazy { TvShowsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteTvShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        getFavoriteTvShow()
    }

    private fun setAdapter() = with(binding) {
        rvDiscoverFavoriteTvShow.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowsAdapter
        }
    }

    private fun getFavoriteTvShow() {
        viewModel.getFavoriteTvShow()?.observe(viewLifecycleOwner) {
            if (it != null) {
                val list = mapList(it)
                tvShowsAdapter.submitList(list)
            }
        }
    }

    private fun mapList(tvShows: List<FavoriteTvShow>): ArrayList<TvShowsMainResult> {

        val listTvShow = ArrayList<TvShowsMainResult>()

        for (tvShow in tvShows) {
            val tvShowMapped = TvShowsMainResult(
                tvShow.tvShowFirstAirDate,
                tvShow.tvShowId,
                tvShow.tvShowTitle,
                tvShow.tvShowPoster,
                tvShow.tvShowRating
            )
            listTvShow.add(tvShowMapped)
        }

        return listTvShow
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}