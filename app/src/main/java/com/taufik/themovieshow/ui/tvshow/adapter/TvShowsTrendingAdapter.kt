package com.taufik.themovieshow.ui.tvshow.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.taufik.themovieshow.R
import com.taufik.themovieshow.api.UrlEndpoint
import com.taufik.themovieshow.databinding.ItemsTvShowsBinding
import com.taufik.themovieshow.ui.tvshow.activity.DetailTvShowActivity
import com.taufik.themovieshow.ui.tvshow.model.trending.TvShowsTrendingResult

class TvShowsTrendingAdapter : RecyclerView.Adapter<TvShowsTrendingAdapter.TvShowsViewHolder>() {

    private var listTvShows = ArrayList<TvShowsTrendingResult>()

    fun setTvShows(tvShowPopularResult: List<TvShowsTrendingResult>) {
        this.listTvShows.clear()
        this.listTvShows.addAll(tvShowPopularResult)
        notifyDataSetChanged()
    }

    inner class TvShowsViewHolder(private val binding: ItemsTvShowsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tvShowPopularResult: TvShowsTrendingResult) {
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
                    val intent = Intent(itemView.context, DetailTvShowActivity::class.java).apply {
                        putExtra(DetailTvShowActivity.EXTRA_DETAIL_ID, tvShowPopularResult.id)
                        putExtra(DetailTvShowActivity.EXTRA_DETAIL_TITLE, tvShowPopularResult.name)
                    }
                    it.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowsViewHolder {
        val itemsMovieShowBinding = ItemsTvShowsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvShowsViewHolder(itemsMovieShowBinding)
    }

    override fun onBindViewHolder(holder: TvShowsViewHolder, position: Int) {
        val pos = listTvShows[position]
        holder.bind(pos)
    }

    override fun getItemCount(): Int = listTvShows.size
}