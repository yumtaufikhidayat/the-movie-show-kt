package com.taufik.themovieshow.data.remote.api

import com.taufik.themovieshow.model.response.common.reviews.ReviewsResponse
import com.taufik.themovieshow.model.response.movie.cast.MovieCastResponse
import com.taufik.themovieshow.model.response.movie.detail.MovieDetailResponse
import com.taufik.themovieshow.model.response.movie.discover.DiscoverMovieResponse
import com.taufik.themovieshow.model.response.movie.genre.GenresResponse
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
import com.taufik.themovieshow.utils.CommonConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(UrlEndpoint.MOVIE_TRENDING_DAY)
    suspend fun getMovieTrendingDay(
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<MovieTrendingResponse>

    @GET(UrlEndpoint.MOVIE_NOW_PLAYING)
    suspend fun getMovieNowPlaying(
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<MovieMainResponse>

    @GET(UrlEndpoint.MOVIE_UPCOMING)
    suspend fun getMovieUpcoming(
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<MovieMainResponse>

    @GET(UrlEndpoint.DISCOVER_MOVIES)
    suspend fun getDiscoverMovie(
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String,
        @Query(CommonConstants.QUERY_Q) query: String
    ): Response<DiscoverMovieResponse>

    @GET(UrlEndpoint.MOVIE_VIDEO)
    suspend fun getMovieVideo(
        @Path(CommonConstants.QUERY_MOVIE_ID) movieId: Int,
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<MovieVideoResponse>

    @GET(UrlEndpoint.MOVIE_CAST)
    suspend fun getMovieCast(
        @Path(CommonConstants.QUERY_MOVIE_ID) movieId: Int,
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<MovieCastResponse>

    @GET(UrlEndpoint.MOVIE_DETAIL)
    suspend fun getDetailMovie(
        @Path(CommonConstants.QUERY_MOVIE_ID) movieId: Int,
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<MovieDetailResponse>

    @GET(UrlEndpoint.MOVIE_REVIEWS)
    suspend fun getReviewsMovie(
        @Path(CommonConstants.QUERY_MOVIE_ID) movieId: Int,
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<ReviewsResponse>

    @GET(UrlEndpoint.MOVIE_SIMILAR)
    suspend fun getSimilarMovie(
        @Path(CommonConstants.QUERY_MOVIE_ID) movieId: Int,
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<MovieSimilarResponse>

    @GET(UrlEndpoint.MOVIE_GENRE)
    suspend fun getGenres(
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<GenresResponse>

    @GET(UrlEndpoint.TV_SHOWS_TRENDING_DAY)
    suspend fun getTvShowsTrending(
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<TvShowsTrendingResponse>

    @GET(UrlEndpoint.TV_SHOWS_AIRING_TODAY)
    suspend fun getTvShowsAiringToday(
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<TvShowsMainResponse>

    @GET(UrlEndpoint.TV_SHOWS_POPULAR)
    suspend fun getTvShowsPopular(
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<TvShowsMainResponse>

    @GET(UrlEndpoint.DISCOVER_TV_SHOWS)
    suspend fun getDiscoverTvShows(
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String,
        @Query(CommonConstants.QUERY_Q) query: String
    ): Response<DiscoverTvShowsResponse>

    @GET(UrlEndpoint.TV_SHOWS_VIDEO)
    suspend fun getTvShowsVideo(
        @Path(CommonConstants.QUERY_TV_SHOW_ID) tvId: Int,
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<TvShowsVideoResponse>

    @GET(UrlEndpoint.TV_SHOWS_CAST)
    suspend fun getTvShowsCast(
        @Path(CommonConstants.QUERY_TV_SHOW_ID) tvId: Int,
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<TvShowsCastResponse>

    @GET(UrlEndpoint.TV_SHOWS_DETAIL)
    suspend fun getDetailTvShows(
        @Path(CommonConstants.QUERY_TV_SHOW_ID) tvId: Int,
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<TvShowsPopularDetailResponse>

    @GET(UrlEndpoint.TV_SHOWS_REVIEWS)
    suspend fun getReviewsTvShows(
        @Path(CommonConstants.QUERY_TV_SHOW_ID) tvId: Int,
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<ReviewsResponse>

    @GET(UrlEndpoint.TV_SHOWS_SIMILAR)
    suspend fun getSimilarTvShows(
        @Path(CommonConstants.QUERY_TV_SHOW_ID) tvId: Int,
        @Query(CommonConstants.QUERY_API_KEY) apiKey: String
    ): Response<TvShowsSimilarResponse>
}