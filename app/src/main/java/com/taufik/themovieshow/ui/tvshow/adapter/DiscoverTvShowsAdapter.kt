package com.taufik.themovieshow.ui.tvshow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResult
import com.taufik.themovieshow.utils.CommonDateFormatConstants.EEE_D_MMM_YYYY_FORMAT
import com.taufik.themovieshow.utils.CommonDateFormatConstants.YYYY_MM_DD_FORMAT
import com.taufik.themovieshow.utils.extensions.convertDate
import com.taufik.themovieshow.utils.extensions.loadImage
import com.taufik.themovieshow.utils.extensions.toRating

class DiscoverTvShowsAdapter(
    private val onItemClickListener: (DiscoverTvShowsResult) -> Unit
) : ListAdapter<DiscoverTvShowsResult, DiscoverTvShowsAdapter.TvShowsViewHolder>(
    DISCOVER_TV_SHOW_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowsViewHolder {
        val itemsMovieShowBinding = ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvShowsViewHolder(itemsMovieShowBinding)
    }

    override fun onBindViewHolder(holder: TvShowsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TvShowsViewHolder(private val binding: ItemsMoviesTvShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DiscoverTvShowsResult) {
            binding.apply {
                imgPoster.loadImage(data.posterPath)
                tvTitle.text = data.name
                tvReleaseDate.text =
                    data.firstAirDate?.convertDate(YYYY_MM_DD_FORMAT, EEE_D_MMM_YYYY_FORMAT)
                        ?: root.context.getString(R.string.tvNA)
                tvRating.text = data.voteAverage.toRating()

                cardMoviesTvShow.setOnClickListener {
                    onItemClickListener(data)
                }
            }
        }
    }

    companion object {
        val DISCOVER_TV_SHOW_CALLBACK = object :
            DiffUtil.ItemCallback<DiscoverTvShowsResult>() {
            override fun areItemsTheSame(
                oldItem: DiscoverTvShowsResult,
                newItem: DiscoverTvShowsResult
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: DiscoverTvShowsResult,
                newItem: DiscoverTvShowsResult
            ): Boolean = oldItem == newItem
        }
    }
}