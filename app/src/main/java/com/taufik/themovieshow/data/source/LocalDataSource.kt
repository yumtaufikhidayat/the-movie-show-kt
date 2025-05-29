package com.taufik.themovieshow.data.source

import android.content.Context
import androidx.lifecycle.LiveData
import com.taufik.themovieshow.data.local.dao.TheMovieShowDao
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.data.local.preferences.language.LanguagePreference
import com.taufik.themovieshow.model.about.AboutSection
import com.taufik.themovieshow.model.favorite.SortFiltering
import com.taufik.themovieshow.utils.objects.UtilsData
import com.taufik.themovieshow.utils.objects.UtilsData.getGeneratedAboutData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val favoriteDao: TheMovieShowDao,
    private val languagePreference: LanguagePreference
) {

    val languageFlow: Flow<String> = languagePreference.languageFlow

    suspend fun addMovieToFavorite(favoriteMovieEntity: FavoriteMovieEntity) =
        favoriteDao.addMovieToFavorite(favoriteMovieEntity)

    fun getFavoriteMovies(rawQuery: RawQuery): LiveData<List<FavoriteMovieEntity>> {
        return favoriteDao.getFavoriteMovie(rawQuery.value)
    }

    suspend fun checkFavoriteMovie(movieId: Int) = favoriteDao.checkMovieFavorite(movieId)

    suspend fun removeMovieFromFavorite(movieId: Int) = favoriteDao.removeMovieFromFavorite(movieId)

    suspend fun addTvShowToFavorite(favoriteTvShowEntity: FavoriteTvShowEntity) =
        favoriteDao.addTvShowToFavorite(favoriteTvShowEntity)

    fun getFavoriteTvShows(rawQuery: RawQuery): LiveData<List<FavoriteTvShowEntity>> {
        return favoriteDao.getFavoriteTvShows(rawQuery.value)
    }

    suspend fun checkFavoriteTvShow(tvShowId: Int) = favoriteDao.checkTvShowFavorite(tvShowId)

    suspend fun removeTvShowFromFavorite(tvShowId: Int) = favoriteDao.removeTvShowFromFavorite(tvShowId)

    fun getAboutData(context: Context): List<AboutSection> = getGeneratedAboutData(context)

    fun getSortFiltering(): List<SortFiltering> = UtilsData.generateSortFilteringData()

    suspend fun setLanguage(code: String) = languagePreference.setLanguage(code)

    suspend fun getLanguage(): String = languagePreference.getLanguage()
}