package com.taufik.themovieshow.utils.objects

import com.taufik.themovieshow.base.helper.BaseDetailData
import com.taufik.themovieshow.model.response.movie.detail.MovieDetailResponse
import com.taufik.themovieshow.model.response.movie.detail.ReleaseDatesResponse
import java.util.Locale

fun getCertificationForUserOrDefault(releaseDates: ReleaseDatesResponse?): String {
    val results = releaseDates?.results ?: return "-"
    val firstWithCert = results
        .firstOrNull { country ->
            country.releaseDates.any {
                it.certification.isNotBlank()
            }
        }

    val usCert = results
        .firstOrNull { it.iso3166_1 == "US" }
        ?.releaseDates
        ?.firstOrNull { it.certification.isNotBlank() }

    return firstWithCert?.releaseDates
        ?.firstOrNull { it.certification.isNotBlank() }
        ?.certification
        ?: usCert?.certification
        ?: "-"
}

fun BaseDetailData.getUserCountryCertification(): String {
    return getCertificationForUserOrDefault(this.releaseDates)
}
