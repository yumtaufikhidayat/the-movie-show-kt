package com.taufik.themovieshow.ui.favorite.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.taufik.themovieshow.R
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.data.source.RawQuery
import com.taufik.themovieshow.utils.CommonConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteMovieViewModel @Inject constructor(
    private val theMovieShowRepository: TheMovieShowRepository
) : ViewModel() {

    @StringRes
    var currentFilter: Int? = null

    private val _getFavoriteMovies = MutableLiveData<RawQuery>()
    val getFavoriteMovies = _getFavoriteMovies.switchMap { theMovieShowRepository.getFavoriteMovieList(it) }

    fun setFavoriteOrder(nameRes: Int) {
        currentFilter = nameRes
        val builder = RawQuery.Companion.Builder()
            .selectAll()
            .from(CommonConstants.TABLE_NAME_FAVORITE_MOVIE_ENTITY)

        val rawQuery = when (nameRes) {
            R.string.tvSortTitle -> builder.orderBy(CommonConstants.COLUMN_NAME_TITLE).build()
            R.string.tvSortRelease -> builder.orderBy(CommonConstants.COLUMN_NAME_RELEASE_DATE).build()
            R.string.tvRating -> builder.orderBy(CommonConstants.COLUMN_NAME_RATING).build()
            else -> builder.build()
        }
        _getFavoriteMovies.value = rawQuery
    }

    fun getSortFiltering() = theMovieShowRepository.getSortFiltering()

    companion object {
        var position: Int = 0
    }
}