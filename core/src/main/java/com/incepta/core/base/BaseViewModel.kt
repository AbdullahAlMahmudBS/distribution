// com/inceptaiddi/core/base/BaseViewModel.kt
package com.inceptaiddi.core.base

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.incepta.core.base.Result
import com.incepta.core.base.UiEvent
import com.incepta.core.base.fold
import com.incepta.core.network.exception.UnauthorizedException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel : ViewModel() {
    protected fun <T> callService(
        serviceCall: suspend () -> Result<T>,
        onStart: () -> Unit = {},
        onSuccess: (T) -> Unit,
        onError: (message: String, canRetry: Boolean) -> Unit = { _, _ -> },
        onCompleted: () -> Unit = {},
        retryCount: Int = 0
    ) {
        viewModelScope.launch {
            var attempts = 0
            while (attempts <= retryCount) {
                //onStart(attempts == 0)
                onStart()
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

    private val _uiEvent = MutableStateFlow<UiEvent?>(null)
    val uiEvent: StateFlow<UiEvent?> = _uiEvent.asStateFlow()

    protected fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        _uiEvent.value = UiEvent.ShowSnackbar(message, actionLabel, duration)
    }

    protected fun showErrorMessage(message: String, canRetry: Boolean = false) {
        _uiEvent.value = UiEvent.ShowErrorMessage(message, canRetry)
    }

    protected fun showWarningMessage(message: String) {
        _uiEvent.value = UiEvent.ShowWarningMessage(message)
    }

    // Clear the event after it's consumed
    fun clearUiEvent() {
        _uiEvent.value = null
    }
}

