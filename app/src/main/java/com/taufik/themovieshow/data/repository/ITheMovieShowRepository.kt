package com.taufik.themovieshow.data.repository

import android.content.Context
import com.taufik.themovieshow.data.NetworkResult
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity
import com.taufik.themovieshow.data.local.entity.tvshow.FavoriteTvShowEntity
import com.taufik.themovieshow.data.source.RawQuery
import com.taufik.themovieshow.model.about.AboutSection
import com.taufik.themovieshow.model.favorite.SortFiltering
import com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse
import com.taufik.themovieshow.model.response.movie.cast.MovieCastResponse
import com.taufik.themovieshow.model.response.movie.detail.MovieDetailResponse
import com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResponse
import com.taufik.themovieshow.model.response.movie.nowplayingupcoming.MovieMainResponse
import com.taufik.themovieshow.model.response.movie.similar.MovieSimilarResponse
import com.taufik.themovieshow.model.response.movie.trending.MovieTrendingResponse
import com.taufik.themovieshow.model.response.movie.video.MovieVideoResponse
import com.taufik.themovieshow.model.response.tvshow.cast.TvShowsCastResponse
import com.taufik.themovieshow.model.response.tvshow.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.model.response.tvshow.discover.DiscoverTvShowsResponse
import com.taufik.themovieshow.model.response.tvshow.popularairingtoday.TvShowsMainResponse
import com.taufik.themovieshow.model.response.tvshow.similar.TvShowsSimilarResponse
import com.taufik.themovieshow.model.response.tvshow.trending.TvShowsTrendingResponse
import com.taufik.themovieshow.model.response.tvshow.video.TvShowsVideoResponse
import kotlinx.coroutines.flow.Flow

interface ITheMovieShowRepository {
    // Remote Data Source
    fun getMovieTrendingDay(): Flow<NetworkResult<MovieTrendingResponse>>
    fun getMovieNowPlaying(): Flow<NetworkResult<MovieMainResponse>>
    fun getMovieUpcoming(): Flow<NetworkResult<MovieMainResponse>>
    fun getDiscoverMovie(query: String): Flow<NetworkResult<DiscoverMovieResponse>>
    fun getMovieVideo(movieId: Int) : Flow<NetworkResult<MovieVideoResponse>>
    fun getMovieCast(movieId: Int): Flow<NetworkResult<MovieCastResponse>>
    fun getDetailMovie(movieId: Int) : Flow<NetworkResult<MovieDetailResponse>>
    fun getMovieReviews(movieId: Int) : Flow<NetworkResult<ReviewsResponse>>
    fun getSimilarMovie(movieId: Int) : Flow<NetworkResult<MovieSimilarResponse>>
    fun getTvShowsTrending(): Flow<NetworkResult<TvShowsTrendingResponse>>
    fun getTvShowsAiringToday(): Flow<NetworkResult<TvShowsMainResponse>>
    fun getTvShowsPopular(): Flow<NetworkResult<TvShowsMainResponse>>
    fun getDiscoverTvShows(query: String): Flow<NetworkResult<DiscoverTvShowsResponse>>
    fun getTvShowsVideo(tvId: Int) : Flow<NetworkResult<TvShowsVideoResponse>>
    fun getTvShowsCast(tvId: Int) : Flow<NetworkResult<TvShowsCastResponse>>
    fun getDetailTvShows(tvId: Int) : Flow<NetworkResult<TvShowsPopularDetailResponse>>
    fun getTvShowsReviews(tvId: Int): Flow<NetworkResult<ReviewsResponse>>
    fun getSimilarTvShows(tvId: Int): Flow<NetworkResult<TvShowsSimilarResponse>>

    // Local Data Source
    suspend fun addMovieToFavorite(favoriteMovieEntity: FavoriteMovieEntity)
    fun getFavoriteMovieList(rawQuery: RawQuery): Flow<List<FavoriteMovieEntity>>
    suspend fun checkFavoriteMovie(movieId: Int): Int
    suspend fun removeMovieFromFavorite(movieId: Int): Int
    suspend fun addTvShowToFavorite(favoriteTvShowEntity: FavoriteTvShowEntity)
    fun getFavoriteTvShows(rawQuery: RawQuery): Flow<List<FavoriteTvShowEntity>>
    suspend fun checkFavoriteTvShow(tvShowId: Int): Int
    suspend fun removeTvShowFromFavorite(tvShowId: Int): Int
    fun getAboutData(context: Context): List<AboutSection>
    fun getSortFiltering(): List<SortFiltering>
    suspend fun setLanguage(code: String, isChanged: Boolean)
    suspend fun getLanguage(): String
    suspend fun setLanguageChangedMessage(isChanged: Boolean)
    fun getLanguageChangedMessage(): Flow<Boolean>
}