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
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(UrlEndpoint.MOVIE_NOW_PLAYING)
    suspend fun getMovieNowPlaying(): Response<MovieMainResponse>

    @GET(UrlEndpoint.MOVIE_UPCOMING)
    suspend fun getMovieUpcoming(): Response<MovieMainResponse>

    @GET(UrlEndpoint.MOVIE_TRENDING_DAY)
    suspend fun getMovieTrendingDay(): Response<MovieTrendingResponse>

    @GET(UrlEndpoint.DISCOVER_MOVIES)
    suspend fun getDiscoverMovie(
        @Query("query") query: String
    ): Response<DiscoverMovieResponse>

    @GET(UrlEndpoint.MOVIE_VIDEO)
    suspend fun getMovieVideo(
        @Path("movie_id") movieId: Int
    ): Response<MovieVideoResponse>

    @GET(UrlEndpoint.MOVIE_CAST)
    suspend fun getMovieCast(
        @Path("movie_id") movieId: Int
    ): Response<MovieCastResponse>

    @GET(UrlEndpoint.MOVIE_DETAIL)
    suspend fun getDetailMovie(
        @Path("movie_id") movieId: Int
    ): Response<MovieDetailResponse>

    @GET(UrlEndpoint.MOVIE_REVIEWS)
    suspend fun getReviewsMovie(
        @Path("movie_id") movieId: Int
    ): Response<ReviewsResponse>

    @GET(UrlEndpoint.MOVIE_SIMILAR)
    suspend fun getSimilarMovie(
        @Path("movie_id") movieId: Int
    ): Response<MovieSimilarResponse>

    @GET(UrlEndpoint.TV_SHOWS_AIRING_TODAY)
    suspend fun getTvShowsAiringToday(): Response<TvShowsMainResponse>

    @GET(UrlEndpoint.TV_SHOWS_POPULAR)
    suspend fun getTvShowsPopular(): Response<TvShowsMainResponse>

    @GET(UrlEndpoint.TV_SHOWS_TRENDING_DAY)
    suspend fun getTvShowsTrending(): Response<TvShowsTrendingResponse>

    @GET(UrlEndpoint.DISCOVER_TV_SHOWS)
    suspend fun getDiscoverTvShows(
        @Query("query") query: String
    ): Response<DiscoverTvShowsResponse>

    @GET(UrlEndpoint.TV_SHOWS_VIDEO)
    suspend fun getTvShowsVideo(
        @Path("tv_id") tvId: Int
    ): Response<TvShowsVideoResponse>

    @GET(UrlEndpoint.TV_SHOWS_CAST)
    suspend fun getTvShowsCast(
        @Path("tv_id") tvId: Int
    ): Response<TvShowsCastResponse>

    @GET(UrlEndpoint.TV_SHOWS_DETAIL)
    suspend fun getDetailTvShows(
        @Path("tv_id") tvId: Int
    ): Response<TvShowsPopularDetailResponse>

    @GET(UrlEndpoint.TV_SHOWS_REVIEWS)
    suspend fun getReviewsTvShows(
        @Path("tv_id") tvId: Int
    ): Response<ReviewsResponse>

    @GET(UrlEndpoint.TV_SHOWS_SIMILAR)
    suspend fun getSimilarTvShows(
        @Path("tv_id") tvId: Int
    ): Response<TvShowsSimilarResponse>
}