package com.example.tienda_bonbin.navigation

sealed class Screen(val route: String) {
    // --- Rutas principales unificadas con el sufijo "_screen" ---
    data object Home : Screen(route = "home_screen")
    data object Profile : Screen(route = "profile_screen")
    data object Catalogo : Screen(route = "catalogo_screen")
    data object Carrito : Screen(route = "carrito_screen")

    // --- Rutas de autenticación unificadas ---
    data object Login : Screen(route = "login_screen")
    data object Registro : Screen(route = "registro_screen")

    // --- Rutas que no se están usando actualmente (puedes eliminarlas o mantenerlas comentadas) ---
    /*
    data object Settings : Screen(route = "settings_screen")
    data object Inicio : Screen(route = "inicio_screen")

    data class Detail(val itemId: String): Screen("detail_screen/{itemId}"){
        fun buildRoute(): String{
            return route.replace("{itemId}",itemId)
        }
    }
    */
}
