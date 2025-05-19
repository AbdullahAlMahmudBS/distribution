package com.incepta.core.base

import androidx.compose.material3.SnackbarDuration


/**
 * Created by Abdullah on 15/5/25.
 *//**
 * Represents a UI event triggered by a ViewModel to display feedback in the UI.
 */
sealed class UiEvent {
    /**
     * Shows a snackbar with a message and optional action.
     */
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val duration: SnackbarDuration = SnackbarDuration.Short
    ) : UiEvent()

    /**
     * Shows an error dialog with a message and optional retry action.
     */
    data class ShowErrorMessage(
        val message: String,
        val canRetry: Boolean = false
    ) : UiEvent()

    /**
     * Shows a warning dialog with a message.
     */
    data class ShowWarningMessage(
        val message: String
    ) : UiEvent()
}