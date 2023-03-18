package com.taufik.themovieshow.data.repository

import com.taufik.themovieshow.data.remote.api.Api
import javax.inject.Inject

class TheMovieShowRepository @Inject constructor(private val api: Api) {
    suspend fun getMovieNowPlaying() = api.getMovieNowPlaying()
    suspend fun getMovieUpcoming() = api.getMovieUpcoming()
    suspend fun getMovieTrendingDay() = api.getMovieTrendingDay()
    suspend fun getDiscoverMovie(query: String) = api.getDiscoverMovie(query)
    suspend fun getMovieVideo(movieId: Int) = api.getMovieVideo(movieId)
    suspend fun getMovieCast(movieId: Int) = api.getMovieCast(movieId)
    suspend fun getDetailMovie(movieId: Int) = api.getDetailMovie(movieId)
    suspend fun getMovieReviews(movieId: Int) = api.getReviewsMovie(movieId)
    suspend fun getSimilarMovie(movieId: Int) = api.getSimilarMovie(movieId)
    suspend fun getTvShowsAiringToday() = api.getTvShowsAiringToday()
    suspend fun getTvShowsPopular() = api.getTvShowsPopular()
    suspend fun getTvShowsTrending() = api.getTvShowsTrending()
    suspend fun getDiscoverTvShows(query: String) = api.getDiscoverTvShows(query)
    suspend fun getTvShowsVideo(tvId: Int) = api.getTvShowsVideo(tvId)
    suspend fun getTvShowsCast(tvId: Int) = api.getTvShowsCast(tvId)
    suspend fun getDetailTvShows(tvId: Int) = api.getDetailTvShows(tvId)
    suspend fun getTvShowsReviews(tvId: Int) = api.getReviewsTvShows(tvId)
    suspend fun getSimilarTvShows(tvId: Int) = api.getSimilarTvShows(tvId)
}