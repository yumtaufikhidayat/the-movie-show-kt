package com.taufik.themovieshow.utils.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
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
                    if (isDebug) {
                        throw IllegalStateException("Success received but data is null. Data: $data")
                    } else {
                        onError?.invoke("Received empty data on success response")
                    }
                }
            }
            is NetworkResult.Error -> onError?.invoke(response.message.orEmpty())
        }
    }
}
