package com.taufik.themovieshow.ui.language.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.databinding.ItemLanguageOptionBinding
import com.taufik.themovieshow.model.language.LanguageOption

class LanguageListAdapter(
    private val onItemSelected: (LanguageOption) -> Unit
) : ListAdapter<LanguageOption, LanguageListAdapter.LanguageViewHolder>(LANGUAGE_DIFF_UTIL) {

    inner class LanguageViewHolder(private val binding: ItemLanguageOptionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LanguageOption) {
            binding.apply {
                tvLanguageLabel.text = item.textLabel
                ivFlag.setImageResource(item.flagResId)
                radioButton.isChecked = item.isSelected

                root.setOnClickListener {
                    onItemSelected.invoke(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding =
            ItemLanguageOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val LANGUAGE_DIFF_UTIL = object : DiffUtil.ItemCallback<LanguageOption>() {
            override fun areItemsTheSame(
                oldItem: LanguageOption,
                newItem: LanguageOption
            ): Boolean {
                return oldItem.code == newItem.code
            }

            override fun areContentsTheSame(
                oldItem: LanguageOption,
                newItem: LanguageOption
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}