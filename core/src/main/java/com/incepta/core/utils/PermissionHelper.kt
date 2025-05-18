package com.incepta.core.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.compose.runtime.saveable.rememberSaveable


data class PermissionHelper(
    val permission: String,
    val isGranted: Boolean,
    val shouldShowRationale: Boolean,
    val requestPermission: () -> Unit
)

@Composable
fun rememberPermissionState(
    permission: String,
    context: Context = androidx.compose.ui.platform.LocalContext.current
): PermissionHelper {
    var permissionGranted by rememberSaveable { mutableStateOf(false) }
    var rationaleNeeded by rememberSaveable { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
        rationaleNeeded = !isGranted
    }

    LaunchedEffect(permission) {
        permissionGranted = ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted) {
            rationaleNeeded = shouldShowRequestPermissionRationale(context, permission)
        }
    }

    return PermissionHelper(
        permission = permission,
        isGranted = permissionGranted,
        shouldShowRationale = rationaleNeeded,
        requestPermission = {
            launcher.launch(permission)
        }
    )
}

private fun shouldShowRequestPermissionRationale(
    context: Context,
    permission: String
): Boolean {
    if (context is ComponentActivity) {
        return context.shouldShowRequestPermissionRationale(permission)
    }
    return false
}
