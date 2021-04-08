package com.taufik.themovieshow.api

import com.taufik.themovieshow.ui.main.movie.data.detail.MovieDetailResponse
import com.taufik.themovieshow.ui.main.movie.data.main.MovieMainResponse
import com.taufik.themovieshow.ui.main.tvshow.data.detail.TvShowsPopularDetailResponse
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

    @GET(UrlEndpoint.MOVIE_DETAIL)
    fun getDetailMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieDetailResponse>

    @GET(UrlEndpoint.TV_SHOWS_POPULAR_DETAIL)
    fun getDetailTvShows(
            @Path("tv_id") tvId: Int,
            @Query("api_key") apiKey: String
    ): Call<TvShowsPopularDetailResponse>
}