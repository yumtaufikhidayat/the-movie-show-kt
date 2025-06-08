package com.taufik.themovieshow.ui.favorite.viewmodel

import androidx.lifecycle.ViewModel
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
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
class FavoriteTvShowViewModel @Inject constructor(
    private val favoriteTvShowRepository: TheMovieShowRepository
) : ViewModel() {

    private val _favoriteTvShowsQuery = MutableStateFlow<RawQuery?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val favoriteTvShowsFlow: Flow<List<FavoriteTvShowEntity>> = _favoriteTvShowsQuery
        .filterNotNull()
        .flatMapLatest { query ->
            favoriteTvShowRepository.getFavoriteTvShows(query)
        }

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
        _favoriteTvShowsQuery.value = rawQuery
    }

    fun getSortFiltering() = favoriteTvShowRepository.getSortFiltering()

    companion object {
        var position = 0
    }
}