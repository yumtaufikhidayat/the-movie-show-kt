package com.taufik.themovieshow.api

import com.taufik.themovieshow.ui.main.movie.data.nowplaying.MovieNowPlayingResponse
import com.taufik.themovieshow.ui.main.movie.data.nowplayingdetail.MovieNowPlayingDetailResponse
import com.taufik.themovieshow.ui.main.tvshow.data.tvshow.TvShowPopularResponse
import com.taufik.themovieshow.ui.main.tvshow.data.tvshowdetail.TvShowsPopularDetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET(UrlEndpoint.MOVIE_NOW_PLAYING)
    fun getMovieNowPlaying(
        @Query("api_key") apiKey: String
    ): Call<MovieNowPlayingResponse>

    @GET(UrlEndpoint.MOVIE_NOW_PLAYING_DETAIL)
    fun getDetailMovieNowPlaying(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieNowPlayingDetailResponse>

    @GET(UrlEndpoint.TV_SHOWS_POPULAR)
    fun getTvShowsPopular(
            @Query("api_key") apiKey: String
    ): Call<TvShowPopularResponse>

    @GET(UrlEndpoint.TV_SHOWS_POPULAR_DETAIL)
    fun getDetailTvShowsPopular(
            @Path("tv_id") tvId: Int,
            @Query("api_key") apiKey: String
    ): Call<TvShowsPopularDetailResponse>
}