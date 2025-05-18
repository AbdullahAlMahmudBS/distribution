package com.incepta.core.service


/**
 * Created by Abdullah on 15/5/25.
 */

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalAppNavController = staticCompositionLocalOf<NavHostController> {
    error("NavController not provided")
}