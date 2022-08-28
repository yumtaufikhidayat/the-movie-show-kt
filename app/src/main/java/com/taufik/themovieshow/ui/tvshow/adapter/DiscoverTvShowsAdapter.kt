package com.taufik.themovieshow.ui.tvshow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.taufik.themovieshow.R
import com.taufik.themovieshow.api.UrlEndpoint
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.ui.tvshow.fragment.DetailTvShowFragment
import com.taufik.themovieshow.ui.tvshow.model.discover.DiscoverTvShowsResult

class DiscoverTvShowsAdapter : RecyclerView.Adapter<DiscoverTvShowsAdapter.TvShowsViewHolder>() {

    private var listTvShows = ArrayList<DiscoverTvShowsResult>()

    fun setTvShows(tvShowPopularResult: List<DiscoverTvShowsResult>) {
        this.listTvShows.clear()
        this.listTvShows.addAll(tvShowPopularResult)
        notifyDataSetChanged()
    }

    inner class TvShowsViewHolder(private val binding: ItemsMoviesTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tvShowPopularResult: DiscoverTvShowsResult) {
            with(binding) {
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
                        DetailTvShowFragment.EXTRA_DETAIL_TV_TITLE to tvShowPopularResult.name
                    )
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