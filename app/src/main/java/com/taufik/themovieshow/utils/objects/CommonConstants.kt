package com.taufik.themovieshow.utils.objects

import com.taufik.themovieshow.BuildConfig

object CommonConstants {
    /* Paging */
    const val STARTING_PAGE_INDEX = 1
    const val LOAD_PER_PAGE = 5
    const val LOAD_MAX_PER_PAGE = 25

    /* Table & Database */
    const val TABLE_NAME_FAVORITE_MOVIE_ENTITY = "favorite_movie"
    const val COLUMN_NAME_POSTER = "poster"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_RELEASE_DATE = "release_date"
    const val COLUMN_NAME_FIRST_AIR_DATE = "first_air_date"
    const val COLUMN_NAME_RATING = "rating"
    const val TABLE_NAME_FAVORITE_TV_SHOW_ENTITY = "favorite_tv_show"
    const val DB_NAME = "TheMovieShowDB"
    const val DB_VERSION = 4
    const val ENCRYPTED_DB_PASSPHRASE = BuildConfig.ENCRYPTED_DB_PASSPHRASE
    const val QUERY_API_KEY = "api_key"
    const val QUERY_Q = "query"
    const val QUERY_MOVIE_ID = "movie_id"
    const val QUERY_TV_SHOW_ID = "tv_id"
    const val QUERY_APPEND_TO_RESPONSE = "append_to_response"

    /* API Key*/
    const val API_KEY = BuildConfig.API_KEY

    /* Profile */
    const val LINKEDIN = "linkedIn"
    const val GOOGLE_PLAY = "google_play"
    const val GITHUB = "github"
    const val EMAIL = "email"
    const val LINKEDIN_URL_LINK = "https://linkedin.com/in/taufik-hidayat"
    const val GOOGLE_PLAY_URL_LINK = "https://play.google.com/store/apps/details?id=com.taufik.themovieshow"
    const val GITHUB_URL_LINK = "https://github.com/yumtaufikhidayat/the-movie-show-kt"
    const val EMAIL_ADDRESS = "yumtaufikhidayat@gmail.com"

    /* Secure Preferences */
    const val KEY_SECURE_PREFS = "secure_prefs"
    const val KEY_PASSPHRASE = "db_passphrase"
    const val KEY_IV = "db_iv"
    const val KEY_STORE_ANDROID = "AndroidKeyStore"
}