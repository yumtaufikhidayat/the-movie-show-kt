package com.taufik.themovieshow.ui.favorite.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.data.source.RawQuery
import com.taufik.themovieshow.utils.CommonConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteTvShowViewModel @Inject constructor(
    private val favoriteTvShowRepository: TheMovieShowRepository
) : ViewModel() {

    private val _getFavoriteTvShows = MutableLiveData<RawQuery>()
    val getFavoriteTvShows = _getFavoriteTvShows.switchMap { favoriteTvShowRepository.getFavoriteTvShows(it) }

    fun setFavoriteOrder(position: Int) {
        val builder = RawQuery.Companion.Builder()
            .selectAll()
            .from(CommonConstants.TABLE_NAME_FAVORITE_TV_SHOW_ENTITY)

        val rawQuery = when (position) {
            1 -> builder.orderBy(CommonConstants.COLUMN_NAME_TITLE, true).build()
            2 -> builder.orderBy(CommonConstants.COLUMN_NAME_FIRST_AIR_DATE, false).build()
            3 -> builder.orderBy(CommonConstants.COLUMN_NAME_RATING, false).build()
            else -> builder.build()
        }
        _getFavoriteTvShows.value = rawQuery

    }

    fun getSortFiltering() = favoriteTvShowRepository.getSortFiltering()

    companion object {
        var position = 0
        const val DELAY_SCROLL_TO_TOP_POSITION = 100L
    }
}