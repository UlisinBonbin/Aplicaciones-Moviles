package com.example.tienda_bonbin.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tienda_bonbin.ui.screen.RegistroScreen
import com.example.tienda_bonbin.ui.screen.ResumenScreen
// Asegúrate de importar AMBOS ViewModels
import com.example.tienda_bonbin.viewmodels.RegistroViewModel
import com.example.tienda_bonbin.viewmodels.UsuarioViewModel

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    // El UsuarioViewModel sigue aquí, lo usará la pantalla de Resumen
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "Registro"
    ){
        composable("Registro") {
            // ¡AQUÍ ESTÁ EL CAMBIO!
            // Creamos una instancia del ViewModel que esta pantalla necesita.
            val registroViewModel: RegistroViewModel = viewModel()
            RegistroScreen(navController, registroViewModel) // Le pasamos el ViewModel correcto
        }
        composable("Resumen") {
            // La pantalla de Resumen sigue usando el UsuarioViewModel
            ResumenScreen(usuarioViewModel)
        }
    }
}
