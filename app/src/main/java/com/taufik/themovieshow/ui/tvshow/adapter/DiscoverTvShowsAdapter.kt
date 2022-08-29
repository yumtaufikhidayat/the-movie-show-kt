package com.taufik.themovieshow.ui.tvshow.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.ui.tvshow.fragment.DetailTvShowFragment
import com.taufik.themovieshow.ui.tvshow.model.discover.DiscoverTvShowsResult
import com.taufik.themovieshow.utils.LoadImage.loadImage

class DiscoverTvShowsAdapter : RecyclerView.Adapter<DiscoverTvShowsAdapter.TvShowsViewHolder>() {

    private var listTvShows = ArrayList<DiscoverTvShowsResult>()

    fun setTvShows(tvShowPopularResult: List<DiscoverTvShowsResult>) {
        this.listTvShows.clear()
        this.listTvShows.addAll(tvShowPopularResult)
        notifyDataSetChanged()
    }

    inner class TvShowsViewHolder(private val binding: ItemsMoviesTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DiscoverTvShowsResult) {
            with(binding) {
                imgPoster.loadImage(data.posterPath)
                tvTitle.text = data.name
                tvReleaseDate.text = data.firstAirDate
                tvRating.text = data.voteAverage.toString()

                itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putParcelable(DetailTvShowFragment.EXTRA_DATA, data)
                    it.findNavController().navigate(R.id.detailTvShowFragment, bundle)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowsViewHolder {
        val itemsMovieShowBinding = ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvShowsViewHolder(itemsMovieShowBinding)
    }

    override fun onBindViewHolder(holder: TvShowsViewHolder, position: Int) {
        val pos = listTvShows[position]
        holder.bind(pos)
    }

    override fun getItemCount(): Int = listTvShows.size
}