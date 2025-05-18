package com.incepta.core.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Created by Abdullah on 15/5/25.
 */

@Composable
fun PermissionRequestDialog(
    permission: String,
    rationaleText: String = "We need this permission to proceed",
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
) {
    var showRationaleDialog by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) onPermissionGranted()
            else onPermissionDenied()
        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(permission)
    }

    if (showRationaleDialog) {
        AlertDialog(
            onDismissRequest = { showRationaleDialog = false },
            title = { Text("Permission Required") },
            text = { Text(rationaleText) },
            confirmButton = {
                TextButton(onClick = {
                    showRationaleDialog = false
                    permissionLauncher.launch(permission)
                }) {
                    Text("Grant")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showRationaleDialog = false
                    onPermissionDenied()
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}
