package com.taufik.themovieshow.utils.enums

import androidx.annotation.StringRes
import com.taufik.themovieshow.R
import com.taufik.themovieshow.base.helper.BaseDetailData
import com.taufik.themovieshow.model.response.movie.detail.ReleaseDatesResponse
import java.util.Locale

enum class RatingCategory(
    val ageLevel: Int, // for comparison
    @StringRes val labelResId: Int
) {
    ALL_AUDIENCES(0, R.string.tvAllAges),
    TEENS(13, R.string.tvTeens),
    MATURE_TEENS(17, R.string.tvMatureTeens),
    ADULTS(21, R.string.tvAdults);

    companion object {
        fun fromCertification(cert: String): RatingCategory {
            val level = normalizeCertification(cert)
            return entries.maxByOrNull { if (it.ageLevel <= level) it.ageLevel else -1 }
                ?: ALL_AUDIENCES
        }

        fun normalizeCertification(code: String): Int {
            return when (code.trim().uppercase()) {
                "SU", "G" -> 0
                "PG", "PG-13", "13+" -> 13
                "17+", "R" -> 17
                "NC-17", "21+" -> 21
                else -> 0
            }
        }
    }
}

fun getCertificationForUserOrDefault(releaseDates: ReleaseDatesResponse?): String {
    val results = releaseDates?.results ?: return "-"
    val userCountry = Locale.getDefault().country.ifBlank { "ID" }

    // First, get certificate from user country
    val userCert = results
        .firstOrNull { it.iso3166_1 == userCountry }
        ?.releaseDates
        ?.mapNotNull { it.certification.takeIf { cert -> cert.isNotBlank() } }
        ?.maxByOrNull { RatingCategory.normalizeCertification(it) }

    if (!userCert.isNullOrBlank()) return userCert

    // Then, if user country is empty, then get from first user country that has valid certificate
    val firstAvailableCert = results
        .asSequence()
        .flatMap { it.releaseDates.asSequence() }
        .mapNotNull { it.certification.takeIf { cert -> cert.isNotBlank() } }
        .maxByOrNull { RatingCategory.normalizeCertification(it) }

    if (!firstAvailableCert.isNullOrBlank()) return firstAvailableCert

    // Lastly, fallback to US
    val usCert = results
        .firstOrNull { it.iso3166_1 == "US" }
        ?.releaseDates
        ?.mapNotNull { it.certification.takeIf { cert -> cert.isNotBlank() } }
        ?.maxByOrNull { RatingCategory.normalizeCertification(it) }

    if (!usCert.isNullOrBlank()) return usCert

    return "-"
}

fun BaseDetailData.getUserCountryCertification(): String {
    return getCertificationForUserOrDefault(this.releaseDates)
}
