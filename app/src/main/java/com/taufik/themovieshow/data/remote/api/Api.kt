package com.taufik.themovieshow.data.remote.api

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
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET(UrlEndpoint.MOVIE_NOW_PLAYING)
    fun getMovieNowPlaying(@Query("api_key") apiKey: String): Call<MovieMainResponse>

    @GET(UrlEndpoint.MOVIE_UPCOMING)
    fun getMovieUpcoming(@Query("api_key") apiKey: String): Call<MovieMainResponse>

    @GET(UrlEndpoint.MOVIE_TRENDING_DAY)
    fun getMovieTrendingDay(@Query("api_key") apiKey: String): Call<MovieTrendingResponse>

    @GET(UrlEndpoint.DISCOVER_MOVIES)
    fun getDiscoverMovie(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<DiscoverMovieResponse>

    @GET(UrlEndpoint.MOVIE_VIDEO)
    fun getMovieVideo(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieVideoResponse>

    @GET(UrlEndpoint.MOVIE_CAST)
    fun getMovieCast(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieCastResponse>

    @GET(UrlEndpoint.MOVIE_DETAIL)
    fun getDetailMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieDetailResponse>

    @GET(UrlEndpoint.MOVIE_REVIEWS)
    fun getReviewsMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<ReviewsResponse>

    @GET(UrlEndpoint.MOVIE_SIMILAR)
    fun getSimilarMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieSimilarResponse>

    @GET(UrlEndpoint.TV_SHOWS_AIRING_TODAY)
    fun getTvShowsAiringToday(
        @Query("api_key") apiKey: String
    ): Call<TvShowsMainResponse>

    @GET(UrlEndpoint.TV_SHOWS_POPULAR)
    fun getTvShowsPopular(
        @Query("api_key") apiKey: String
    ): Call<TvShowsMainResponse>

    @GET(UrlEndpoint.TV_SHOWS_TRENDING_DAY)
    fun getTvShowsTrending(
        @Query("api_key") apiKey: String
    ): Call<TvShowsTrendingResponse>

    @GET(UrlEndpoint.DISCOVER_TV_SHOWS)
    fun getDiscoverTvShows(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<DiscoverTvShowsResponse>

    @GET(UrlEndpoint.TV_SHOWS_VIDEO)
    fun getTvShowsVideo(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): Call<TvShowsVideoResponse>

    @GET(UrlEndpoint.TV_SHOWS_CAST)
    fun getTvShowsCast(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): Call<TvShowsCastResponse>

    @GET(UrlEndpoint.TV_SHOWS_DETAIL)
    fun getDetailTvShows(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): Call<TvShowsPopularDetailResponse>

    @GET(UrlEndpoint.TV_SHOWS_REVIEWS)
    fun getReviewsTvShows(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String
    ): Call<ReviewsResponse>

    @GET(UrlEndpoint.TV_SHOWS_SIMILAR)
    fun getSimilarTvShows(
        @Path("tv_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<TvShowsSimilarResponse>
}