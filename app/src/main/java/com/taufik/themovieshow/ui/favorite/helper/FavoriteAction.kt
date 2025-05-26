package com.taufik.themovieshow.ui.favorite.helper

data class FavoriteAction(
    val addFavorite: (Int, String, String, String, Double) -> Unit,
    val removeFavorite: (Int) -> Unit
)