package com.taufik.themovieshow.ui.about.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.databinding.ItemAboutBinding
import com.taufik.themovieshow.model.response.about.About

class AboutApplicationAdapter(
    private val onItemClick: (Int) -> Unit
) : ListAdapter<About, AboutApplicationAdapter.AboutViewHolder>(aboutApplicationDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder {
        return AboutViewHolder(
            ItemAboutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AboutViewHolder(private val binding: ItemAboutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(about: About) {
            binding.apply {
                imgAbout.setImageResource(about.imgAbout)
                tvAboutTitle.text = about.titleAbout
                tvAboutDesc.text = about.descAbout

                itemView.setOnClickListener {
                    onItemClick(layoutPosition)
                }
            }
        }
    }

    companion object {
        val aboutApplicationDiffCallback = object :
            DiffUtil.ItemCallback<About>() {
            override fun areItemsTheSame(
                oldItem: About,
                newItem: About
            ): Boolean = oldItem.titleAbout == newItem.titleAbout

            override fun areContentsTheSame(
                oldItem: About,
                newItem: About
            ): Boolean = oldItem == newItem
        }
    }
}