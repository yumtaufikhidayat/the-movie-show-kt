package com.taufik.themovieshow.api

import com.taufik.themovieshow.ui.feature.movie.data.detail.MovieDetailResponse
import com.taufik.themovieshow.ui.feature.movie.data.discover.DiscoverMovieResponse
import com.taufik.themovieshow.ui.feature.movie.data.nowplayingupcoming.MovieMainResponse
import com.taufik.themovieshow.ui.feature.movie.data.trending.MovieTrendingResponse
import com.taufik.themovieshow.ui.feature.tvshow.data.detail.TvShowsPopularDetailResponse
import com.taufik.themovieshow.ui.feature.tvshow.data.discover.DiscoverTvShowsResponse
import com.taufik.themovieshow.ui.feature.tvshow.data.popularairingtoday.TvShowsMainResponse
import com.taufik.themovieshow.ui.feature.tvshow.data.trending.TvShowsTrendingReponse
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

    @GET(UrlEndpoint.TV_SHOWS_DETAIL)
    fun getDetailTvShows(
            @Path("tv_id") tvId: Int,
            @Query("api_key") apiKey: String
    ): Call<TvShowsPopularDetailResponse>
}