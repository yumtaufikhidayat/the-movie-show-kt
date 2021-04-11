package com.taufik.themovieshow.ui.feature.movie.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.taufik.themovieshow.R
import com.taufik.themovieshow.api.UrlEndpoint
import com.taufik.themovieshow.databinding.ItemCastBinding
import com.taufik.themovieshow.ui.feature.movie.data.cast.MovieCast

class MovieCastAdapter : RecyclerView.Adapter<MovieCastAdapter.MovieViewHolder>(){

    private var listMovieCast = ArrayList<MovieCast>()

    fun setMovieCasts(movieCast: ArrayList<MovieCast>) {
        this.listMovieCast.clear()
        this.listMovieCast.addAll(movieCast)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder (private val binding: ItemCastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieCast: MovieCast) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(UrlEndpoint.IMAGE_URL + movieCast.profilePath)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .into(imgPoster)

                tvCastName.text = movieCast.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemsCastBinding = ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemsCastBinding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val pos = listMovieCast[position]
        holder.bind(pos)
    }

    override fun getItemCount(): Int = listMovieCast.size
}