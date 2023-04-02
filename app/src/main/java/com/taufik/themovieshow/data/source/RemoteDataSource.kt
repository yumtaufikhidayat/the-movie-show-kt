package com.taufik.themovieshow.data.source

import com.taufik.themovieshow.data.remote.api.ApiService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getMovieNowPlaying() = apiService.getMovieNowPlaying()
    suspend fun getMovieUpcoming() = apiService.getMovieUpcoming()
    suspend fun getMovieTrendingDay() = apiService.getMovieTrendingDay()
    suspend fun getDiscoverMovie(query: String) = apiService.getDiscoverMovie(query)
    suspend fun getMovieVideo(movieId: Int) = apiService.getMovieVideo(movieId)
    suspend fun getMovieCast(movieId: Int) = apiService.getMovieCast(movieId)
    suspend fun getDetailMovie(movieId: Int) = apiService.getDetailMovie(movieId)
    suspend fun getMovieReviews(movieId: Int) = apiService.getReviewsMovie(movieId)
    suspend fun getSimilarMovie(movieId: Int) = apiService.getSimilarMovie(movieId)
    suspend fun getTvShowsAiringToday() = apiService.getTvShowsAiringToday()
    suspend fun getTvShowsPopular() = apiService.getTvShowsPopular()
    suspend fun getTvShowsTrending() = apiService.getTvShowsTrending()
    suspend fun getDiscoverTvShows(query: String) = apiService.getDiscoverTvShows(query)
    suspend fun getTvShowsVideo(tvId: Int) = apiService.getTvShowsVideo(tvId)
    suspend fun getTvShowsCast(tvId: Int) = apiService.getTvShowsCast(tvId)
    suspend fun getDetailTvShows(tvId: Int) = apiService.getDetailTvShows(tvId)
    suspend fun getTvShowsReviews(tvId: Int) = apiService.getReviewsTvShows(tvId)
    suspend fun getSimilarTvShows(tvId: Int) = apiService.getSimilarTvShows(tvId)
}