package com.incepta.core.base


/**
 * Created by Abdullah on 15/5/25.
 */
sealed class UiEvent {
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val onAction: (() -> Unit)? = null
    ) : UiEvent()
}
