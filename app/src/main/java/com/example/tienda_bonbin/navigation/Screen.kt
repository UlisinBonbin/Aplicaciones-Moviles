package com.example.tienda_bonbin.navigation

sealed class Screen(val route: String) {
    data object Home: Screen(route="home")
    data object Profile: Screen(route = "profile")
    data object Settings : Screen(route = "settings")

    data object Registro: Screen(route = "registro")
    //agregar pantallas luego, por ejemplo; Sobre nosotros, catalogo, etc.

    data class Detail(val itemId: String): Screen("detail_page/{itemId}"){
        fun buildRoute(): String{
            return route.replace("{itemId}",itemId)
        }
    }

    //esto es especialmente para el catálogo, para mostrar un producto (itemId) dentro de una página (catálogo)
}