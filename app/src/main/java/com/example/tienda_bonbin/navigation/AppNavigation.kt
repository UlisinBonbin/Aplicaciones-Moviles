package com.example.tienda_bonbin.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tienda_bonbin.ui.screen.HomeScreen
import com.example.tienda_bonbin.ui.screen.LoginScreen
import com.example.tienda_bonbin.ui.screen.ProfileScreen
import com.example.tienda_bonbin.ui.screen.RegistroScreen
import com.example.tienda_bonbin.viewmodels.AppViewModelProvider
import com.example.tienda_bonbin.viewmodels.MainViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Creamos una instancia del MainViewModel que gestionará la navegación
    val mainViewModel: MainViewModel = viewModel()

    // --- LÍNEA ELIMINADA ---
    // mainViewModel.navController = navController // Ya no es necesaria

    // NavHost es el componente que gestiona las "pantallas" de tu app
    NavHost(
        navController = navController,
        // La pantalla inicial será HomeScreen
        startDestination = Screen.Home.route
    ) {
        // ... (el resto del código permanece exactamente igual)

        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                viewModel = mainViewModel
            )
        }
        // ... etc
    }
}
