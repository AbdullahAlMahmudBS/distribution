package com.incepta.msfa.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.incepta.core.service.LocalAppNavController
import com.incepta.msfa.features.location.LocationScreen
import com.incepta.msfa.features.post.presentation.screens.PostScreen

/**
 * Created by Abdullah on 15/5/25.
 */

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {

    CompositionLocalProvider(LocalAppNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = Route.HomeScreen.route
        ) {
            composable(Route.HomeScreen.route) { PostScreen() }
            composable(Route.LocationScreen.route) { LocationScreen() }
        }
    }
}
