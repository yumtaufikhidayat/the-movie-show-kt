package com.taufik.themovieshow.utils.extensions

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.taufik.themovieshow.R

fun NavController.navigateReplacingSplash(
    destinationId: Int,
    builder: (NavOptionsBuilder.() -> Unit)? = null
) {
    this.navigate(
        destinationId,
        null,
        navOptions {
            popUpTo(R.id.splashScreenFragment) {
                inclusive = true
            }
            launchSingleTop = true
            builder?.invoke(this)
        }
    )
}
