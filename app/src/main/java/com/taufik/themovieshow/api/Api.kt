package com.taufik.themovieshow.api

import com.taufik.themovieshow.ui.main.movie.data.main.MovieResponse
import com.taufik.themovieshow.ui.main.movie.data.detail.MovieDetailResponse
import com.taufik.themovieshow.ui.main.tvshow.data.popular.TvShowPopularResponse
import com.taufik.themovieshow.ui.main.tvshow.data.populardetail.TvShowsPopularDetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET(UrlEndpoint.MOVIE_NOW_PLAYING)
    fun getMovieNowPlaying(
        @Query("api_key") apiKey: String
    ): Call<MovieResponse>

    @GET(UrlEndpoint.MOVIE_UPCOMING)
    fun getMovieUpcoming(
        @Query("api_key") apiKey: String
    ): Call<MovieResponse>

    @GET(UrlEndpoint.MOVIE_NOW_PLAYING_DETAIL)
    fun getDetailMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieDetailResponse>

    @GET(UrlEndpoint.TV_SHOWS_POPULAR)
    fun getTvShowsPopular(
            @Query("api_key") apiKey: String
    ): Call<TvShowPopularResponse>

    @GET(UrlEndpoint.TV_SHOWS_AIRING_TODAY)
    fun getTvShowsAiringToday(
            @Query("api_key") apiKey: String
    ): Call<TvShowPopularResponse>

    @GET(UrlEndpoint.TV_SHOWS_POPULAR_DETAIL)
    fun getDetailTvShows(
            @Path("tv_id") tvId: Int,
            @Query("api_key") apiKey: String
    ): Call<TvShowsPopularDetailResponse>
}