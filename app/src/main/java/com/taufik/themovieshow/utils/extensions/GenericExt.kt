package com.taufik.themovieshow.utils.extensions

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale

inline fun <T> List<T>.filterAndSortByDate(
    crossinline getDate: (T) -> String?,
    dateThreshold: String = "01-01-2010",
    inputFormat: String,
    thresholdFormat: String
): List<T> {
    val inputFormatter = SimpleDateFormat(inputFormat, Locale.US)
    val thresholdFormatter = SimpleDateFormat(thresholdFormat, Locale.US)

    val parsedThresholdDate = try {
        thresholdFormatter.parse(dateThreshold)
    } catch (e: Exception) {
        Log.e("TAG Filter", "filterAndSortByDateError: ${e.localizedMessage}")
        null
    } ?: return emptyList()

    return this.mapNotNull { item ->
        val dateStr = getDate(item)
        val parsedDate = try {
            dateStr?.let { inputFormatter.parse(it) }
        } catch (e: Exception) {
            Log.e("TAG Filter", "filterAndSortByDateError: ${e.localizedMessage}")
            null
        }

        if (parsedDate != null && (parsedDate >= parsedThresholdDate)) {
            item to parsedDate
        } else null
    }.sortedByDescending {
        it.second
    }.map {
        it.first
    }
}