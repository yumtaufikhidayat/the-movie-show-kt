package com.taufik.themovieshow.utils.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.data.NetworkResult

fun <T> LiveData<NetworkResult<T>>.observeNetworkResult(
    lifecycleOwner: LifecycleOwner,
    isDebug: Boolean = BuildConfig.DEBUG,
    onLoading: (() -> Unit)? = null,
    onSuccess: (T) -> Unit,
    onError: ((String) -> Unit)? = null
) {
    observe(lifecycleOwner) { response ->
        when (response) {
            is NetworkResult.Loading -> onLoading?.invoke()
            is NetworkResult.Success -> {
                val data = response.data
                if (data != null) {
                    onSuccess(data)
                } else {
                    val message = "Success received but data is null."
                    FirebaseCrashlytics.getInstance().apply {
                        log(message)
                        setCustomKey("response_data", "null")
                        recordException(IllegalStateException(message))
                    }

                    if (isDebug) {
                        throw IllegalStateException(message)
                    } else {
                        onError?.invoke("Received empty data on success response")
                    }
                }
            }
            is NetworkResult.Error -> {
                val errorMessage = response.message.orEmpty()
                FirebaseCrashlytics.getInstance().apply {
                    log(errorMessage)
                    setCustomKey("error_message", errorMessage)
                    recordException(RuntimeException("NetworkResult.Error: $errorMessage"))
                }
                onError?.invoke(errorMessage)
            }
        }
    }
}
