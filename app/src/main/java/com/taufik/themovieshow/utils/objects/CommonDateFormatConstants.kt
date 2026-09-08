package com.taufik.themovieshow.utils.objects

object CommonDateFormatConstants {
    const val YYYY_MM_DD_FORMAT = "yyyy-MM-dd"
    const val DD_MM_YYYY_FORMAT = "dd-MM-yyyy"
    const val EEE_D_MMM_YYYY_FORMAT = "EEE, d MMM yyyy"
    const val YYYY_FORMAT = "yyyy"
    const val FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val FULL_FORMAT_WITHOUT_MILLIS = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    // TMDB sends review timestamps both with and without milliseconds
    val FULL_FORMATS = listOf(FULL_FORMAT, FULL_FORMAT_WITHOUT_MILLIS)
}
