package com.taufik.themovieshow.utils.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.taufik.themovieshow.BuildConfig
import com.taufik.themovieshow.data.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> Flow<NetworkResult<T>>.observeNetworkResult(
    lifecycleOwner: LifecycleOwner,
    isDebug: Boolean = BuildConfig.DEBUG,
    onLoading: (() -> Unit)? = null,
    onSuccess: (T) -> Unit,
    onError: ((String) -> Unit)? = null
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collectLatest { response ->
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
                    }
                }
            }
        }
    }
}
