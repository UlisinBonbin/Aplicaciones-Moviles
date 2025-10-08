package com.example.tienda_bonbin.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tienda_bonbin.ui.screen.RegistroScreen
import com.example.tienda_bonbin.ui.screen.ResumenScreen
import com.example.tienda_bonbin.viewmodels.UsuarioViewModel

@Composable
fun AppNavigation(){
    val navController= rememberNavController()
    val usuarioViewModel: UsuarioViewModel= viewModel()

    NavHost(
        navController=navController,
        startDestination = "Registro"
    ){
        composable("Registro") {
            RegistroScreen(navController, usuarioViewModel)
        }
        composable("Resumen") {
            ResumenScreen(usuarioViewModel)
        }
    }
}