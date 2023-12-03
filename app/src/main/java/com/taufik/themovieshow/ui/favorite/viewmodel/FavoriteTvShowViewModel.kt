package com.taufik.themovieshow.ui.favorite.viewmodel

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
class FavoriteTvShowViewModel @Inject constructor(
    private val favoriteTvShowRepository: TheMovieShowRepository
) : ViewModel() {

    private val _getAllFavoriteTvShows = favoriteTvShowRepository.getAllFavoriteTvShows()
    val getAllFavoriteTvShows = _getAllFavoriteTvShows

    private val _getFavoriteTvShowsByTitle = favoriteTvShowRepository.getFavoriteTvShowsByTitle()
    val getFavoriteTvShowsByTitle = _getFavoriteTvShowsByTitle

    private val _getFavoriteTvShowsByRelease = favoriteTvShowRepository.getFavoriteTvShowsByRelease()
    val getFavoriteTvShowsByRelease = _getFavoriteTvShowsByRelease

    private val _getFavoriteTvShowsByRating = favoriteTvShowRepository.getFavoriteTvShowsByRating()
    val getFavoriteTvShowsByRating = _getFavoriteTvShowsByRating

    private val _getFavoriteTvShows = MutableLiveData<RawQuery>()
    val getFavoriteTvShows = _getFavoriteTvShows.switchMap { favoriteTvShowRepository.getFavoriteTvShows(it) }



    fun getSortFiltering() = favoriteTvShowRepository.getSortFiltering()

    fun setFavoriteOrder(nameRes: Int) {
        val builder = RawQuery.Companion.Builder()
            .selectAll()
            .from(CommonConstants.TABLE_NAME_FAVORITE_TV_SHOW_ENTITY)

        val rawQuery = when (nameRes) {
            R.string.tvSortTitle -> builder.orderBy(CommonConstants.COLUMN_NAME_TITLE).build()
            R.string.tvSortRelease -> builder.orderBy(CommonConstants.COLUMN_NAME_RELEASE_DATE).build()

            R.string.tvRating -> builder.orderBy(CommonConstants.COLUMN_NAME_RATING).build()
            else -> builder.build()
        }
        _getFavoriteTvShows.value = rawQuery

    }


    companion object {
        var position = 0
    }
}