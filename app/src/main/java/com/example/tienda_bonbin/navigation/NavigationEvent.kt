package com.example.tienda_bonbin.navigation

import android.health.connect.datatypes.ExerciseRoute

sealed class NavigationEvent {
    data class NavigateTo(
        val route: Screen,
        val popUpToRoute: Screen?=null,
        val inclusive: Boolean = false,
        val singleTop: Boolean = false
    ) : NavigationEvent()

    object PopBackStack: NavigationEvent()
    object NavigateUp: NavigationEvent()
}