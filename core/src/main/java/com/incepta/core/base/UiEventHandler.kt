package com.incepta.core.base

import androidx.compose.ui.Alignment
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier


/**
 * Created by Abdullah on 19/5/25.
 */


/**
 * Created by Abdullah on 19/5/25.
 */

@Composable
fun UiEventHandler(
    uiEvent: UiEvent?,
    onEventConsumed: () -> Unit,
    onSnackbarAction: () -> Unit = {},
    onErrorRetry: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showErrorDialog by remember { mutableStateOf(false) }
    var showWarningDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var canRetry by remember { mutableStateOf(false) }

    LaunchedEffect(uiEvent) {
        when (uiEvent) {
            is UiEvent.ShowSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = uiEvent.message,
                        actionLabel = uiEvent.actionLabel,
                        duration = uiEvent.duration
                    )
                    onEventConsumed()
                }
            }
            is UiEvent.ShowErrorMessage -> {
                dialogMessage = uiEvent.message
                canRetry = uiEvent.canRetry
                showErrorDialog = true
                onEventConsumed()
            }
            is UiEvent.ShowWarningMessage -> {
                dialogMessage = uiEvent.message
                showWarningDialog = true
                onEventConsumed()
            }
            null -> {}
        }
    }

    // Snackbar Host

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier,
        snackbar = {
            Snackbar(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(16.dp),
                action = {
                    if (it.visuals.actionLabel != null) {
                        TextButton(onClick = { onSnackbarAction(); it.dismiss() }) {
                            Text(it.visuals.actionLabel!!)
                        }
                    }
                },
                content = { Text(it.visuals.message) }
            )
        }
    )

    // Error Dialog
    AnimatedVisibility(
        visible = showErrorDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error", style = MaterialTheme.typography.titleLarge) },
            text = { Text(dialogMessage, style = MaterialTheme.typography.bodyMedium) },
            confirmButton = {
                if (canRetry) {
                    TextButton(onClick = {
                        onErrorRetry()
                        showErrorDialog = false
                    }) {
                        Text("Retry")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("Dismiss")
                }
            }
        )
    }

    // Warning Dialog
    AnimatedVisibility(
        visible = showWarningDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        AlertDialog(
            onDismissRequest = { showWarningDialog = false },
            title = { Text("Warning", style = MaterialTheme.typography.titleLarge) },
            text = { Text(dialogMessage, style = MaterialTheme.typography.bodyMedium) },
            confirmButton = {
                TextButton(onClick = { showWarningDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}