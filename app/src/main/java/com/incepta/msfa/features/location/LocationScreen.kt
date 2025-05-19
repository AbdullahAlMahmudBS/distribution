package com.incepta.msfa.features.location

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.incepta.core.presentation.components.PermissionRequestDialog
import com.incepta.msfa.navigation.Route


/**
 * Created by Abdullah on 15/5/25.
 */
@Composable
fun LocationScreen (){
    var showPermissionDialog by remember { mutableStateOf(false) }
    var hasPermission by remember { mutableStateOf(false) }

    // Use the PermissionRequestDialog composable
    if (showPermissionDialog && !hasPermission) {
        PermissionRequestDialog(
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            rationaleText = "Location access is needed to show nearby theaters.",
            onPermissionGranted = {
                hasPermission = true
                showPermissionDialog = false
            },
            onPermissionDenied = {
                hasPermission = false
                showPermissionDialog = false
            }
        )
    }

    Size(
        width = 100f,
        height = 100f
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .statusBarsPadding()
    ) {
        Text(
            text = "Location Screen",
            style = MaterialTheme.typography.headlineLarge,
            color = colorResource(id = com.incepta.msfa.R.color.purple_500),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Location Permission: ${if (hasPermission) "Granted" else "Denied"}",
            style = MaterialTheme.typography.bodyLarge,
            color = colorResource(id = com.incepta.msfa.R.color.purple_500),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                showPermissionDialog = true
            },
            modifier = Modifier
                .padding(all = 40.dp)
        ) {
            Text(text = "Request Location Permission")
        }
    }

}