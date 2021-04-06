package com.taufik.themovieshow.ui.detail.movie.viewmodel

import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.data.MovieShowDetail
import com.taufik.themovieshow.utils.DataDummy
import kotlin.properties.Delegates

class DetailViewModel : ViewModel() {

    private var id by Delegates.notNull<Int>()

    fun setSelectedDetail(id: Int) {
        this.id = id
    }

    fun getSelectedDetail(): List<MovieShowDetail> = DataDummy.generateMoviesDetail(id)
}