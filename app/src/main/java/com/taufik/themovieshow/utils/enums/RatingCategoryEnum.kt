package com.taufik.themovieshow.utils.enums

import androidx.annotation.StringRes
import com.taufik.themovieshow.R

enum class RatingCategory(val codes: List<String>, @StringRes val labelResId: Int) {
    ALL_AUDIENCES(listOf("SU", "G"), R.string.tvAllAges),
    TEENS(listOf("PG", "PG-13", "13+", "17+"), R.string.tvTeens),
    ADULTS(listOf("R", "NC-17", "21+"), R.string.tvAdults);

    companion object {
        fun fromCode(code: String): RatingCategory {
            val normalized = code.trim().uppercase()
            return entries.firstOrNull { it.codes.contains(normalized) }
                ?: ALL_AUDIENCES // fallback
        }
    }
}
