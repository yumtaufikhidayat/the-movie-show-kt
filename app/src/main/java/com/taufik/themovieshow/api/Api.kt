package com.taufik.themovieshow.api

import com.taufik.themovieshow.ui.main.movie.data.movie.MovieResponse
import com.taufik.themovieshow.ui.main.movie.data.moviedetail.MovieDetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET(UrlEndpoint.MOVIE_NOW_PLAYING)
    fun getMovieNowPlaying(
        @Query("api_key") apiKey: String
    ): Call<MovieResponse>

    @GET(UrlEndpoint.MOVIE_NOW_PLAYING_DETAIL)
    fun getDetailMovieNowPlaying(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieDetailResponse>
}