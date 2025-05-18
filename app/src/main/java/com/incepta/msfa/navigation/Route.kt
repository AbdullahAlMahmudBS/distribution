package com.incepta.msfa.navigation


/**
 * Created by Abdullah on 15/5/25.
 */

sealed class Route(
    val route : String
) {

    object OnBoardingScreen : Route(route = "onBoardingScreen")

    object HomeScreen : Route(route = "homeScreen")
    object LocationScreen : Route(route = "locationScreen")
}