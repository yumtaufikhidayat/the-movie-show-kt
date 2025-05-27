package com.taufik.themovieshow.utils.helper

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

class DynamicSnapHelper : LinearSnapHelper() {
    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        if (layoutManager !is LinearLayoutManager) return null

        val firstVisible = layoutManager.findFirstVisibleItemPosition()
        val lastVisible = layoutManager.findLastVisibleItemPosition()
        val totalCount = layoutManager.itemCount

        return when {
            firstVisible == 0 -> layoutManager.findViewByPosition(firstVisible) // snap start
            lastVisible == totalCount - 1 -> layoutManager.findViewByPosition(lastVisible) // snap end
            else -> super.findSnapView(layoutManager) // default center
        }
    }
}