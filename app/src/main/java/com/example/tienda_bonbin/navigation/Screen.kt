package com.example.tienda_bonbin.navigation

sealed class Screen(val route: String) {

    data object Home : Screen(route = "home_screen")
    data object Profile : Screen(route = "profile_screen")
    data object Catalogo : Screen(route = "catalogo_screen")
    data object Carrito : Screen(route = "carrito_screen")

    // --- Rutas de autenticación unificadas ---
    data object Login : Screen(route = "login_screen")
    data object Registro : Screen(route = "registro_screen")

}
