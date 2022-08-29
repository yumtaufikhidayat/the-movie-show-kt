package com.taufik.themovieshow.ui.main.movie.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.taufik.themovieshow.R
import com.taufik.themovieshow.api.UrlEndpoint
import com.taufik.themovieshow.databinding.ItemsMoviesTvShowBinding
import com.taufik.themovieshow.ui.main.movie.activity.DetailMovieActivity
import com.taufik.themovieshow.data.main.movie.discover.DiscoverMovieResult

class DiscoverMovieAdapter : RecyclerView.Adapter<DiscoverMovieAdapter.MovieViewHolder>(){

    private var listMovies = ArrayList<DiscoverMovieResult>()

    fun setMovies(movieResult: ArrayList<DiscoverMovieResult>) {
        this.listMovies.clear()
        this.listMovies.addAll(movieResult)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder (private val binding: ItemsMoviesTvShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieResult: DiscoverMovieResult) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(UrlEndpoint.IMAGE_URL + movieResult.posterPath)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(imgPoster)

                tvTitle.text = movieResult.title
                tvReleaseDate.text = movieResult.releaseDate
                tvRating.text = movieResult.voteAverage.toString()

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailMovieActivity::class.java).apply {
                        putExtra(DetailMovieActivity.EXTRA_DETAIL_ID, movieResult.id)
                        putExtra(DetailMovieActivity.EXTRA_DETAIL_TITLE, movieResult.title)
                    }
                    it.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemsMovieBinding = ItemsMoviesTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemsMovieBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val pos = listMovies[position]
        holder.bind(pos)
    }

    override fun getItemCount(): Int = listMovies.size
}