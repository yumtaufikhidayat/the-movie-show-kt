package com.taufik.themovieshow.data.remote.api

object UrlEndpoint {
    const val THUMBNAIL_IMAGE_JPG = "/maxresdefault.jpg"
    const val MOVIE_NOW_PLAYING = "movie/now_playing"
    const val MOVIE_UPCOMING = "movie/upcoming"
    const val MOVIE_TRENDING_DAY = "trending/movie/day"
    const val MOVIE_DETAIL = "movie/{movie_id}"
    const val MOVIE_VIDEO = "movie/{movie_id}/videos"
    const val MOVIE_CAST = "movie/{movie_id}/credits"
    const val MOVIE_REVIEWS = "movie/{movie_id}/reviews"
    const val MOVIE_SIMILAR = "movie/{movie_id}/similar"
    const val TV_SHOWS_POPULAR = "tv/popular"
    const val TV_SHOWS_AIRING_TODAY = "tv/airing_today"
    const val TV_SHOWS_TRENDING_DAY = "trending/tv/day"
    const val TV_SHOWS_DETAIL = "tv/{tv_id}"
    const val TV_SHOWS_VIDEO = "tv/{tv_id}/videos"
    const val TV_SHOWS_CAST = "tv/{tv_id}/credits"
    const val TV_SHOWS_REVIEWS = "tv/{tv_id}/reviews"
    const val TV_SHOWS_SIMILAR = "tv/{tv_id}/similar"
    const val DISCOVER_MOVIES = "search/movie"
    const val DISCOVER_TV_SHOWS = "search/tv"
}