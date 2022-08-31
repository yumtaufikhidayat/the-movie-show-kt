package com.taufik.themovieshow.ui.main.tvshow.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.api.UrlEndpoint
import com.taufik.themovieshow.data.main.tvshow.trending.TvShowsTrendingResult
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.ui.detail.tvshow.fragment.DetailTvShowFragment
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating

class TvShowsTrendingAdapter : ListAdapter<TvShowsTrendingResult, TvShowsTrendingAdapter.TvShowsViewHolder>(TvShowTrendingDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowsViewHolder {
        return TvShowsViewHolder(ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TvShowsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TvShowsViewHolder(private val binding: ItemsMoviesTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TvShowsTrendingResult) = with(binding) {
            imgPoster.loadImage(UrlEndpoint.IMAGE_URL + data.posterPath)
            tvTitle.text = data.name
            tvReleaseDate.text = data.firstAirDate
            tvRating.text = toRating(data.voteAverage)

            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable(DetailTvShowFragment.EXTRA_DATA, data)
                it.findNavController().navigate(R.id.detailTvShowFragment, bundle)
            }
        }
    }

    companion object {
        object TvShowTrendingDiffCallback: DiffUtil.ItemCallback<TvShowsTrendingResult>(){
            override fun areItemsTheSame(
                oldItem: TvShowsTrendingResult,
                newItem: TvShowsTrendingResult
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: TvShowsTrendingResult,
                newItem: TvShowsTrendingResult
            ): Boolean = oldItem == newItem
        }
    }
}