package com.taufik.themovieshow.utils.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

inline fun <T> List<T>.filterAndSortByDate(
    crossinline getDate: (T) -> String?,
    dateThreshold: String = "01-01-2010",
    inputFormat: String,
    thresholdFormat: String
): List<T> {
    val dateThresholdParsed = SimpleDateFormat(thresholdFormat, Locale.US).parse(dateThreshold)

    return this.filter { item ->
        try {
            val dateString = getDate(item)
            if (dateString != null) {
                val itemDate = SimpleDateFormat(inputFormat, Locale.US).parse(dateString)
                itemDate != null && (itemDate.after(dateThresholdParsed) || itemDate == dateThresholdParsed)
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }.sortedByDescending { item ->
        try {
            val dateString = getDate(item)
            if (dateString != null) {
                SimpleDateFormat(inputFormat, Locale.US).parse(dateString)
            } else {
                Date(0) // Default to the earliest possible date if parsing fails
            }
        } catch (e: Exception) {
            Date(0) // Default to the earliest possible date if parsing fails
        }
    }
}