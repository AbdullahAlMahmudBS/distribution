package com.incepta.core.base


/**
 * Created by Abdullah on 18/5/25.
 */


sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    data class Loading(val isInitialLoad: Boolean = true) : UiState<Nothing>()
    data class Success<T>( val datas: T) : UiState<T>()
    data class Error(val message: String, val canRetry: Boolean = true) : UiState<Nothing>()
    data class Empty(val message: String = "No data available") : UiState<Nothing>()

    val isLoading: Boolean
        get() = this is Loading

    val errorMessage: String?
        get() = when (this) {
            is Error -> message
            is Empty -> message
            else -> null
        }

    val data: T?
        get() = if (this is Success) datas else null


}