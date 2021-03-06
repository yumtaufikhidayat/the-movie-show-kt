package com.taufik.themovieshow.api

import com.taufik.themovieshow.ui.movie.model.cast.MovieCastResponse
import com.taufik.themovieshow.ui.movie.model.detail.MovieDetailResponse
import com.taufik.themovieshow.ui.movie.model.discover.DiscoverMovieResponse
import com.taufik.themovieshow.ui.movie.model.nowplayingupcoming.MovieMainResponse
import com.taufik.themovieshow.ui.movie.model.trending.MovieTrendingResponse
import com.taufik.themovieshow.ui.movie.model.video.MovieVideoResponse
import com.taufik.themovieshow.ui.tvshow.model.cast.TvShowsCastResponse
import com.taufik.themovieshow.ui.tvshow.model.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.ui.tvshow.model.discover.DiscoverTvShowsResponse
import com.taufik.themovieshow.ui.tvshow.model.popularairingtoday.TvShowsMainResponse
import com.taufik.themovieshow.ui.tvshow.model.trending.TvShowsTrendingReponse
import com.taufik.themovieshow.ui.tvshow.model.video.TvShowsVideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET(UrlEndpoint.MOVIE_NOW_PLAYING)
    fun getMovieNowPlaying(
            @Query("api_key") apiKey: String
    ): Call<MovieMainResponse>

    @GET(UrlEndpoint.MOVIE_UPCOMING)
    fun getMovieUpcoming(
            @Query("api_key") apiKey: String
    ): Call<MovieMainResponse>

    @GET(UrlEndpoint.MOVIE_TRENDING_DAY)
    fun getMovieTrendingDay(
            @Query("api_key") apiKey: String
    ): Call<MovieTrendingResponse>

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
    ): Call<TvShowsTrendingReponse>

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
}