package com.taufik.themovieshow.model.language

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LanguageOption(
    val code: String,
    val textLabel: String,
    val flagResId: Int,
    val isSelected: Boolean
): Parcelable