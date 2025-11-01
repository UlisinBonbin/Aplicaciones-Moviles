package com.example.tienda_bonbin.navigation/*  <-- INICIO DEL COMENTARIO DE BLOQUE

sealed class AppRoute(val route: String) {
    data object Home: AppRoute("home")
    data object Register: AppRoute("register")
    data object Profile: AppRoute("profile")
    data object Settings: AppRoute("settings")
    data object PantallaPrincipal: AppRoute("pantallaprincipal")
    data object Login: AppRoute("login")
    object Carrito : Screen("carrito_screen")

    data class Detail (val itemId:String): AppRoute("detail/{itemId}")
    {
        fun buildRoute():String{
            return route.replace("{itemId}",itemId)
        }
    }
}

FIN DEL COMENTARIO DE BLOQUE --> */

//APPROUTE NO SE OCUPA, SE ESTA OCUPANDO SCREEN.