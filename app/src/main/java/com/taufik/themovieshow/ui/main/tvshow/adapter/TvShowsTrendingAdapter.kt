package com.taufik.themovieshow.ui.main.tvshow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.data.remote.api.UrlEndpoint
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingResult
import com.taufik.themovieshow.utils.CommonDateFormatConstants
import com.taufik.themovieshow.utils.convertDate
import com.taufik.themovieshow.utils.loadImage
import com.taufik.themovieshow.utils.toRating

class TvShowsTrendingAdapter(
    private val onItemClickListener: (TvShowsTrendingResult) -> Unit
) : PagingDataAdapter<TvShowsTrendingResult, TvShowsTrendingAdapter.TvShowsViewHolder>(TvShowTrendingDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowsViewHolder {
        return TvShowsViewHolder(
            ItemsMoviesTvShowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TvShowsViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) holder.bind(data)
        holder.setIsRecyclable(false)
    }

    inner class TvShowsViewHolder(private val binding: ItemsMoviesTvShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TvShowsTrendingResult) {
            binding.apply {
                imgPoster.loadImage(UrlEndpoint.IMAGE_URL + data.posterPath)
                tvTitle.text = data.name
                tvReleaseDate.text = data.firstAirDate.convertDate(
                    CommonDateFormatConstants.YYYY_MM_DD_FORMAT,
                    CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
                )
                tvRating.text = toRating(data.voteAverage)

                itemView.setOnClickListener {
                    onItemClickListener(data)
                }
            }
        }
    }

    companion object {
        val TvShowTrendingDiffCallback = object: DiffUtil.ItemCallback<TvShowsTrendingResult>() {
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