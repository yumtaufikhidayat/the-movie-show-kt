package com.taufik.themovieshow.ui.tvshow.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.api.UrlEndpoint
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.ui.tvshow.fragment.DetailTvShowFavoriteFragment
import com.taufik.themovieshow.ui.tvshow.model.popularairingtoday.TvShowsMainResult
import com.taufik.themovieshow.utils.LoadImage.loadImage

class TvShowsAdapter : ListAdapter<TvShowsMainResult, TvShowsAdapter.TvShowsViewHolder>(TvShowsCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowsViewHolder {
        val itemsMovieShowBinding = ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvShowsViewHolder(itemsMovieShowBinding)
    }

    override fun onBindViewHolder(holder: TvShowsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TvShowsViewHolder(private val binding: ItemsMoviesTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TvShowsMainResult) {
            with(binding) {
                imgPoster.loadImage(UrlEndpoint.IMAGE_URL + data.posterPath)
                tvTitle.text = data.name
                tvReleaseDate.text = data.firstAirDate
                tvRating.text = data.voteAverage.toString()

                itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putParcelable(DetailTvShowFavoriteFragment.EXTRA_DATA, data)
                    it.findNavController().navigate(R.id.detailTvShowFavoriteFragment, bundle)
                }
            }
        }
    }

    object TvShowsCallback: DiffUtil.ItemCallback<TvShowsMainResult>() {
        override fun areItemsTheSame(
            oldItem: TvShowsMainResult,
            newItem: TvShowsMainResult
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: TvShowsMainResult,
            newItem: TvShowsMainResult
        ): Boolean = oldItem == newItem
    }
}