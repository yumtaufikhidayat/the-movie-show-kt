package com.taufik.themovieshow.ui.tvshow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.taufik.themovieshow.R
import com.taufik.themovieshow.api.UrlEndpoint
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.ui.tvshow.fragment.DetailTvShowFragment
import com.taufik.themovieshow.ui.tvshow.model.trending.TvShowsTrendingResult

class TvShowsTrendingAdapter : ListAdapter<TvShowsTrendingResult, TvShowsTrendingAdapter.TvShowsViewHolder>(TvShowTrendingDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowsViewHolder {
        return TvShowsViewHolder(ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TvShowsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TvShowsViewHolder(private val binding: ItemsMoviesTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tvShowPopularResult: TvShowsTrendingResult) = with(binding) {
                Glide.with(itemView.context)
                    .load(UrlEndpoint.IMAGE_URL + tvShowPopularResult.posterPath)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(imgPoster)

                tvTitle.text = tvShowPopularResult.name
                tvReleaseDate.text = tvShowPopularResult.firstAirDate
                tvRating.text = tvShowPopularResult.voteAverage.toString()

                itemView.setOnClickListener {
                    val bundle = bundleOf(
                        DetailTvShowFragment.EXTRA_DETAIL_TV_ID to tvShowPopularResult.id,
                        DetailTvShowFragment.EXTRA_DETAIL_TV_TITLE to tvShowPopularResult.name,
                    )
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