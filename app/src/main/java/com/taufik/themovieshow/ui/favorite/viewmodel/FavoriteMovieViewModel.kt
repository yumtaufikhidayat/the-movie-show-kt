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
class FavoriteMovieViewModel @Inject constructor(
    private val theMovieShowRepository: TheMovieShowRepository
) : ViewModel() {

    private val _getFavoriteMovies = MutableLiveData<RawQuery>()
    val getFavoriteMovies = _getFavoriteMovies.switchMap {
        theMovieShowRepository.getFavoriteMovieList(it)
    }

    fun setFavoriteOrder(position: Int) {
        val builder = RawQuery.Companion.Builder()
            .selectAll()
            .from(CommonConstants.TABLE_NAME_FAVORITE_MOVIE_ENTITY)

        val rawQuery = when (position) {
            1 -> builder.orderBy(CommonConstants.COLUMN_NAME_TITLE, true).build()
            2 -> builder.orderBy(CommonConstants.COLUMN_NAME_RELEASE_DATE, false).build()
            3 -> builder.orderBy(CommonConstants.COLUMN_NAME_RATING, false).build()
            else -> builder.build()
        }
        _getFavoriteMovies.value = rawQuery
    }

    fun getSortFiltering() = theMovieShowRepository.getSortFiltering()

    companion object {
        var position: Int = 0
    }
}