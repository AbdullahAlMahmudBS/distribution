package com.incepta.msfa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.incepta.msfa.navigation.AppNavGraph
import com.incepta.msfa.ui.theme.InceptaMsfaNewTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Make content draw behind system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        setContent {
            InceptaMsfaNewTheme(
                // Dynamic color is available on Android 12+
                dynamicColor = true,
                // Set darkTheme to false to use light theme
                darkTheme = false
            ) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    // Your NavGraph drives all of the screens
                    AppNavGraph()
                }
            }
        }
    }
}
