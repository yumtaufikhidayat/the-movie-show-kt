package com.taufik.themovieshow.model.favorite

import androidx.annotation.StringRes


data class SortFiltering(
    val sortId: Int = 0,
    @StringRes val sortNameRes: Int = 0
)