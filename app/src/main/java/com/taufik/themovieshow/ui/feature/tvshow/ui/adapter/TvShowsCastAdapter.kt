package com.taufik.themovieshow.ui.feature.tvshow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.taufik.themovieshow.R
import com.taufik.themovieshow.api.UrlEndpoint
import com.taufik.themovieshow.databinding.ItemCastBinding
import com.taufik.themovieshow.ui.feature.tvshow.data.cast.TvShowsCast

class TvShowsCastAdapter : RecyclerView.Adapter<TvShowsCastAdapter.MovieViewHolder>(){

    private var listTvShowsCast = ArrayList<TvShowsCast>()

    fun setTvShowsCasts(movieCast: ArrayList<TvShowsCast>) {
        this.listTvShowsCast.clear()
        this.listTvShowsCast.addAll(movieCast)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder (private val binding: ItemCastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieCast: TvShowsCast) {
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
        val pos = listTvShowsCast[position]
        holder.bind(pos)
    }

    override fun getItemCount(): Int = listTvShowsCast.size
}