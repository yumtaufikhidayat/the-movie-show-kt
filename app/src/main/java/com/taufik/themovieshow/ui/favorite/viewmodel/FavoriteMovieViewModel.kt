package com.taufik.themovieshow.ui.favorite.viewmodel

import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.repository.TheMovieShowRepository
import com.taufik.themovieshow.data.source.RawQuery
import com.taufik.themovieshow.utils.objects.CommonConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class FavoriteMovieViewModel @Inject constructor(
    private val theMovieShowRepository: TheMovieShowRepository
) : ViewModel() {

    private val _favoriteMovieQuery = MutableStateFlow<RawQuery?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteMoviesFlow: Flow<List<FavoriteMovieEntity>> = _favoriteMovieQuery
        .filterNotNull()
        .flatMapLatest { query ->
            theMovieShowRepository.getFavoriteMovieList(query)
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
        _favoriteMovieQuery.value = rawQuery
    }

    fun getSortFiltering() = theMovieShowRepository.getSortFiltering()

    companion object {
        var position: Int = 0
    }
}