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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakeTheMovieShowRepository : ITheMovieShowRepository {

    // Remote flows & handlers
    var movieTrendingDayFlow: Flow<NetworkResult<MovieTrendingResponse>> = flowOf(NetworkResult.Loading())
    var movieNowPlayingFlow: Flow<NetworkResult<MovieMainResponse>> = flowOf(NetworkResult.Loading())
    var movieUpcomingFlow: Flow<NetworkResult<MovieMainResponse>> = flowOf(NetworkResult.Loading())
    var discoverMovieHandler: (String) -> Flow<NetworkResult<DiscoverMovieResponse>> = { flowOf(NetworkResult.Loading()) }
    var movieVideoFlow: Flow<NetworkResult<MovieVideoResponse>> = flowOf(NetworkResult.Loading())
    var movieCastFlow: Flow<NetworkResult<MovieCastResponse>> = flowOf(NetworkResult.Loading())
    var detailMovieFlow: Flow<NetworkResult<MovieDetailResponse>> = flowOf(NetworkResult.Loading())
    var movieReviewsFlow: Flow<NetworkResult<ReviewsResponse>> = flowOf(NetworkResult.Loading())
    var similarMovieFlow: Flow<NetworkResult<MovieSimilarResponse>> = flowOf(NetworkResult.Loading())

    var tvShowsTrendingFlow: Flow<NetworkResult<TvShowsTrendingResponse>> = flowOf(NetworkResult.Loading())
    var tvShowsAiringTodayFlow: Flow<NetworkResult<TvShowsMainResponse>> = flowOf(NetworkResult.Loading())
    var tvShowsPopularFlow: Flow<NetworkResult<TvShowsMainResponse>> = flowOf(NetworkResult.Loading())
    var discoverTvShowsHandler: (String) -> Flow<NetworkResult<DiscoverTvShowsResponse>> = { flowOf(NetworkResult.Loading()) }
    var tvShowsVideoFlow: Flow<NetworkResult<TvShowsVideoResponse>> = flowOf(NetworkResult.Loading())
    var tvShowsCastFlow: Flow<NetworkResult<TvShowsCastResponse>> = flowOf(NetworkResult.Loading())
    var detailTvShowsFlow: Flow<NetworkResult<TvShowsPopularDetailResponse>> = flowOf(NetworkResult.Loading())
    var tvShowsReviewsFlow: Flow<NetworkResult<ReviewsResponse>> = flowOf(NetworkResult.Loading())
    var similarTvShowsFlow: Flow<NetworkResult<TvShowsSimilarResponse>> = flowOf(NetworkResult.Loading())

    // Remote overrides
    override fun getMovieTrendingDay(): Flow<NetworkResult<MovieTrendingResponse>> = movieTrendingDayFlow
    override fun getMovieNowPlaying(): Flow<NetworkResult<MovieMainResponse>> = movieNowPlayingFlow
    override fun getMovieUpcoming(): Flow<NetworkResult<MovieMainResponse>> = movieUpcomingFlow
    override fun getDiscoverMovie(query: String): Flow<NetworkResult<DiscoverMovieResponse>> = discoverMovieHandler(query)
    override fun getMovieVideo(movieId: Int): Flow<NetworkResult<MovieVideoResponse>> = movieVideoFlow
    override fun getMovieCast(movieId: Int): Flow<NetworkResult<MovieCastResponse>> = movieCastFlow
    override fun getDetailMovie(movieId: Int): Flow<NetworkResult<MovieDetailResponse>> = detailMovieFlow
    override fun getMovieReviews(movieId: Int): Flow<NetworkResult<ReviewsResponse>> = movieReviewsFlow
    override fun getSimilarMovie(movieId: Int): Flow<NetworkResult<MovieSimilarResponse>> = similarMovieFlow

    override fun getTvShowsTrending(): Flow<NetworkResult<TvShowsTrendingResponse>> = tvShowsTrendingFlow
    override fun getTvShowsAiringToday(): Flow<NetworkResult<TvShowsMainResponse>> = tvShowsAiringTodayFlow
    override fun getTvShowsPopular(): Flow<NetworkResult<TvShowsMainResponse>> = tvShowsPopularFlow
    override fun getDiscoverTvShows(query: String): Flow<NetworkResult<DiscoverTvShowsResponse>> = discoverTvShowsHandler(query)
    override fun getTvShowsVideo(tvId: Int): Flow<NetworkResult<TvShowsVideoResponse>> = tvShowsVideoFlow
    override fun getTvShowsCast(tvId: Int): Flow<NetworkResult<TvShowsCastResponse>> = tvShowsCastFlow
    override fun getDetailTvShows(tvId: Int): Flow<NetworkResult<TvShowsPopularDetailResponse>> = detailTvShowsFlow
    override fun getTvShowsReviews(tvId: Int): Flow<NetworkResult<ReviewsResponse>> = tvShowsReviewsFlow
    override fun getSimilarTvShows(tvId: Int): Flow<NetworkResult<TvShowsSimilarResponse>> = similarTvShowsFlow

    // Local in-memory store
    val favoriteMovies = mutableListOf<FavoriteMovieEntity>()
    val favoriteTvShows = mutableListOf<FavoriteTvShowEntity>()

    var favoriteMovieListFlow: Flow<List<FavoriteMovieEntity>>? = null
    var favoriteTvShowsListFlow: Flow<List<FavoriteTvShowEntity>>? = null
    var lastMovieRawQuery: RawQuery? = null
    var lastTvShowRawQuery: RawQuery? = null

    var currentLanguage: String = "en"
    override val languageFlow: MutableStateFlow<String> = MutableStateFlow("en")
    private val languageChangedFlow = MutableStateFlow(false)

    var aboutData: List<AboutSection> = emptyList()
    var sortFilteringList: List<SortFiltering> = emptyList()

    override suspend fun addMovieToFavorite(favoriteMovieEntity: FavoriteMovieEntity) {
        favoriteMovies.removeAll { it.movieId == favoriteMovieEntity.movieId }
        favoriteMovies.add(favoriteMovieEntity)
    }

    override fun getFavoriteMovieList(rawQuery: RawQuery): Flow<List<FavoriteMovieEntity>> {
        lastMovieRawQuery = rawQuery
        return favoriteMovieListFlow ?: flowOf(favoriteMovies.toList())
    }

    override suspend fun checkFavoriteMovie(movieId: Int): Int {
        return if (favoriteMovies.any { it.movieId == movieId }) 1 else 0
    }

    override suspend fun removeMovieFromFavorite(movieId: Int): Int {
        val count = favoriteMovies.count { it.movieId == movieId }
        favoriteMovies.removeAll { it.movieId == movieId }
        return count
    }

    override suspend fun addTvShowToFavorite(favoriteTvShowEntity: FavoriteTvShowEntity) {
        favoriteTvShows.removeAll { it.tvShowId == favoriteTvShowEntity.tvShowId }
        favoriteTvShows.add(favoriteTvShowEntity)
    }

    override fun getFavoriteTvShows(rawQuery: RawQuery): Flow<List<FavoriteTvShowEntity>> {
        lastTvShowRawQuery = rawQuery
        return favoriteTvShowsListFlow ?: flowOf(favoriteTvShows.toList())
    }

    override suspend fun checkFavoriteTvShow(tvShowId: Int): Int {
        return if (favoriteTvShows.any { it.tvShowId == tvShowId }) 1 else 0
    }

    override suspend fun removeTvShowFromFavorite(tvShowId: Int): Int {
        val count = favoriteTvShows.count { it.tvShowId == tvShowId }
        favoriteTvShows.removeAll { it.tvShowId == tvShowId }
        return count
    }

    override fun getAboutData(context: Context): List<AboutSection> = aboutData

    override fun getSortFiltering(): List<SortFiltering> = sortFilteringList

    override suspend fun setLanguage(code: String, isChanged: Boolean) {
        currentLanguage = code
        languageFlow.value = code
        setLanguageChangedMessage(isChanged)
    }

    override suspend fun getLanguage(): String = currentLanguage

    override suspend fun setLanguageChangedMessage(isChanged: Boolean) {
        languageChangedFlow.value = isChanged
    }

    override fun getLanguageChangedMessage(): Flow<Boolean> = languageChangedFlow
}
