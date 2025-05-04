package com.taufik.themovieshow.ui.about.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.databinding.ItemAboutSectionBinding
import com.taufik.themovieshow.model.about.AboutSection
import com.taufik.themovieshow.model.response.about.About

class AboutParentAdapter(
    private val onItemClick: (About) -> Unit
) : ListAdapter<AboutSection, AboutParentAdapter.SectionViewHolder>(SectionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding = ItemAboutSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SectionViewHolder(private val binding: ItemAboutSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val childAdapter = AboutChildAdapter(onItemClick)

        fun bind(section: AboutSection) {
            val (title, items) = when (section) {
                is AboutSection.AuthorSection -> section.title to section.items
                is AboutSection.ApplicationSection -> section.title to section.items
            }

            binding.tvTitle.text = title
            binding.rvChild.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = childAdapter
                isNestedScrollingEnabled = false
            }

            childAdapter.submitList(items)
        }
    }

    companion object {
        private val SectionDiffCallback = object : DiffUtil.ItemCallback<AboutSection>() {
            override fun areItemsTheSame(oldItem: AboutSection, newItem: AboutSection): Boolean {
                return when {
                    oldItem is AboutSection.AuthorSection && newItem is AboutSection.AuthorSection ->
                        oldItem.title == newItem.title
                    oldItem is AboutSection.ApplicationSection && newItem is AboutSection.ApplicationSection ->
                        oldItem.title == newItem.title
                    else -> false
                }
            }

            override fun areContentsTheSame(oldItem: AboutSection, newItem: AboutSection): Boolean = oldItem == newItem
        }
    }
}