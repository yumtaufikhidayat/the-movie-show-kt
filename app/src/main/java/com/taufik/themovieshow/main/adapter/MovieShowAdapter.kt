package com.taufik.themovieshow.main.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.MovieShow
import com.taufik.themovieshow.databinding.ItemsMovieShowBinding
import com.taufik.themovieshow.detail.activity.DetailActivity
import com.taufik.themovieshow.utils.Utils

class MovieShowAdapter : RecyclerView.Adapter<MovieShowAdapter.MovieViewHolder>(){

    private var listMovies = ArrayList<MovieShow>()

    fun setMovie(movieShows: List<MovieShow>) {
        this.listMovies.clear()
        this.listMovies.addAll(movieShows)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder (private val binding: ItemsMovieShowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieShow: MovieShow) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(Utils.IMAGE_URL + movieShow.imagePoster)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(imgPoster)

                tvTitle.text = movieShow.title
                tvReleaseDate.text = movieShow.releaseDate
                tvRating.text = movieShow.rate.toString()

                itemView.setOnClickListener {
//                    val intent = Intent(itemView.context, DetailActivity::class.java).apply {
//                        putExtra(DetailActivity.EXTRA_DETAIL_ID, movieShow.id)
//                        putExtra(DetailActivity.EXTRA_DETAIL_TITLE, movieShow.title)
//                    }
//                    it.context.startActivity(intent)
                    val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                        putExtra(DetailActivity.EXTRA_DETAIL_TITLE, movieShow.title)
                    }
                    it.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemsMovieBinding = ItemsMovieShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemsMovieBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val pos = listMovies[position]
        holder.bind(pos)
    }

    override fun getItemCount(): Int = listMovies.size
}