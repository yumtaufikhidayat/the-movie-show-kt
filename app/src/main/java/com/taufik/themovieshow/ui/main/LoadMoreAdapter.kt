package com.taufik.themovieshow.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.taufik.themovieshow.databinding.LoadMoreBinding

class LoadMoreAdapter(private val retry: () -> Unit): LoadStateAdapter<LoadMoreAdapter.LoadMoreViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadMoreAdapter.LoadMoreViewHolder {
        return LoadMoreViewHolder(LoadMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: LoadMoreAdapter.LoadMoreViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)

    inner class LoadMoreViewHolder(private val binding: LoadMoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(state: LoadState) {
            binding.apply {
                pbLoadMore.isVisible = state is LoadState.Loading
                tvLoadMoreError.isVisible = state is LoadState.Error
                btnRetry.isVisible = state is LoadState.Error
                btnRetry.setOnClickListener {
                    retry()
                }
            }
        }
    }
}