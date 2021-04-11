package com.taufik.themovieshow.api

object UrlEndpoint {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_URL = "https://image.tmdb.org/t/p/w780/"
    const val MOVIE_NOW_PLAYING = "movie/now_playing"
    const val MOVIE_UPCOMING = "movie/upcoming"
    const val MOVIE_TRENDING_DAY = "trending/movie/day"
    const val MOVIE_DETAIL = "movie/{movie_id}"
    const val TV_SHOWS_POPULAR = "tv/popular"
    const val TV_SHOWS_AIRING_TODAY = "tv/airing_today"
    const val TV_SHOWS_TRENDING_DAY = "trending/tv/day"
    const val TV_SHOWS_DETAIL = "tv/{tv_id}"
    const val DISCOVER_MOVIES = "search/movie"
    const val DISCOVER_TV_SHOWS = "search/tv"
}