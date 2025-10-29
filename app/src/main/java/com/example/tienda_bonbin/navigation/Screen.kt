package com.example.tienda_bonbin.navigation

sealed class Screen(val route: String) {
    data object Home: Screen(route="home")
    data object Profile: Screen(route = "profile")
    data object Settings : Screen(route = "settings")

    data object Registro: Screen(route = "registro")
    data object Inicio: Screen(route = "inicio")
    //agregar pantallas luego, por ejemplo; Sobre nosotros, catalogo, etc.

    data class Detail(val itemId: String): Screen("detail_page/{itemId}"){
        fun buildRoute(): String{
            return route.replace("{itemId}",itemId)
        }
    }
    data object Catalogo : Screen("catalogo")

    data object Login : Screen("login")
}