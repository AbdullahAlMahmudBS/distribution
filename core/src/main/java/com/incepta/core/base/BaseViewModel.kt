// com/inceptaiddi/core/base/BaseViewModel.kt
package com.inceptaiddi.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.incepta.core.base.Result
import com.incepta.core.base.fold
import com.incepta.core.network.exception.UnauthorizedException

abstract class BaseViewModel : ViewModel() {
    protected fun <T> callService(
        serviceCall: suspend () -> Result<T>,
        onStart: (isInitialLoad: Boolean) -> Unit,
        onSuccess: (T) -> Unit,
        onError: (message: String, canRetry: Boolean) -> Unit,
        onCompleted: () -> Unit,
        retryCount: Int = 0
    ) {
        viewModelScope.launch {
            var attempts = 0
            while (attempts <= retryCount) {
                onStart(attempts == 0)
                try {
                    serviceCall().fold(
                        onSuccess = { data ->
                            onSuccess(data)
                            onCompleted()
                            return@launch
                        },
                        onFailure = { error ->
                            if (attempts < retryCount) {
                                attempts++
                                delay(1000L * attempts) // Exponential backoff
                            } else {
                                onError(error.message ?: "Unknown error", error !is UnauthorizedException)
                                onCompleted()
                                return@launch
                            }
                        }
                    )
                } catch (e: Exception) {
                    if (attempts < retryCount) {
                        attempts++
                        delay(1000L * attempts)
                    } else {
                        onError(e.message ?: "Unknown error", e !is UnauthorizedException)
                        onCompleted()
                        return@launch
                    }
                }
            }
        }
    }
}

