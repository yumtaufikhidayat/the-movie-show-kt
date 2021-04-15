package com.taufik.themovieshow.ui.feature.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.taufik.themovieshow.databinding.FragmentFavoriteTvShowsBinding
import com.taufik.themovieshow.ui.feature.tvshow.data.local.FavoriteTvShow
import com.taufik.themovieshow.ui.feature.tvshow.data.popularairingtoday.TvShowsMainResult
import com.taufik.themovieshow.ui.feature.tvshow.ui.adapter.TvShowsAdapter
import com.taufik.themovieshow.ui.feature.tvshow.viewmodel.FavoriteTvShowViewModel

class FavoriteTvShowsFragment : Fragment() {

    private lateinit var favoriteTvShowBinding: FragmentFavoriteTvShowsBinding
    private lateinit var tvShowsAdapter: TvShowsAdapter
    private lateinit var viewModel: FavoriteTvShowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        favoriteTvShowBinding = FragmentFavoriteTvShowsBinding.inflate(inflater, container, false)
        return favoriteTvShowBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()

        setViewModel()

        setData()

        getFavoriteTvShow()
    }

    private fun setAdapter() {
        tvShowsAdapter = TvShowsAdapter()
        tvShowsAdapter.notifyDataSetChanged()
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(requireActivity())[FavoriteTvShowViewModel::class.java]
    }

    private fun setData() {
        with(favoriteTvShowBinding.rvDiscoverFavoriteTvShow) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = tvShowsAdapter
        }
    }

    private fun getFavoriteTvShow() {
        viewModel.getFavoriteTvShow()?.observe(viewLifecycleOwner, {
            if (it != null) {
                val list = mapList(it)
                tvShowsAdapter.setTvShows(list)
            }
        })
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
}