package com.taufik.themovieshow.ui.favorite.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.R
import com.taufik.themovieshow.databinding.ItemSortFilterBinding
import com.taufik.themovieshow.model.favorite.SortFiltering

class SortFilteringAdapter(
    private var onItemClickListener: ((Int) -> Unit)? = null
): ListAdapter<SortFiltering, SortFilteringAdapter.ViewHolder>(SORT_FILTERING_DIFF_CALLBACK) {

    private var selectedItemPosition = RecyclerView.NO_POSITION

    fun setDefaultSelectedItemPosition(position: Int) {
        if (position in 0 until itemCount) {
            selectedItemPosition = position
            notifyItemChanged(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSortFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.selectedBackground()
        if (position == selectedItemPosition) holder.selectedBackground() else holder.defaultBackground()
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(private val binding: ItemSortFilterBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SortFiltering, position: Int) {
            binding.apply {
                tvSortFilterName.text = itemView.context.getString(data.sortNameRes)
                itemView.isSelected = position == selectedItemPosition

                cardSortFiltering.setOnClickListener {
                    if (selectedItemPosition != position) {
                        val previousSelected = selectedItemPosition
                        selectedItemPosition = position

                        notifyItemChanged(previousSelected)
                        notifyItemChanged(selectedItemPosition)

                        onItemClickListener?.invoke(data.sortNameRes)
                    }
                }
            }
        }

        fun defaultBackground() {
            binding.apply {
                cardSortFiltering.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_unselected)
                with(tvSortFilterName) {
                    setTextColor(ContextCompat.getColor(itemView.context, R.color.colorTextPrimary))
                    typeface = Typeface.DEFAULT
                }
            }
        }

        fun selectedBackground() {
            binding.apply {
                cardSortFiltering.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_selected)
                with(tvSortFilterName) {
                    setTextColor(ContextCompat.getColor(itemView.context, R.color.colorTextOther))
                    typeface = Typeface.DEFAULT_BOLD
                }
            }
        }
    }

    companion object {
        val SORT_FILTERING_DIFF_CALLBACK = object : DiffUtil.ItemCallback<SortFiltering>() {
            override fun areItemsTheSame(
                oldItem: SortFiltering,
                newItem: SortFiltering
            ): Boolean = oldItem.sortId == newItem.sortId

            override fun areContentsTheSame(
                oldItem: SortFiltering,
                newItem: SortFiltering
            ): Boolean = oldItem == newItem
        }
    }
}