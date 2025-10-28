package com.example.tienda_bonbin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tienda_bonbin.navigation.NavigationEvent // <-- Importación necesaria
import com.example.tienda_bonbin.navigation.Screen
import com.example.tienda_bonbin.ui.screen.HomeScreen
import com.example.tienda_bonbin.ui.screen.InicioScreen
import com.example.tienda_bonbin.ui.screen.PantallaPrincial
import com.example.tienda_bonbin.ui.screen.ProfileScreen
import com.example.tienda_bonbin.ui.screen.RegistroScreen
import com.example.tienda_bonbin.ui.screen.SettingsScreen
import com.example.tienda_bonbin.ui.theme.TiendaBonbinTheme
import com.example.tienda_bonbin.viewmodels.EstadoViewModel
import com.example.tienda_bonbin.viewmodels.MainViewModel
import com.example.tienda_bonbin.viewmodels.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            TiendaBonbinTheme {
                // Contenedor principal que organiza toda la navegación de la app.
                AppContainer()
            }
        }
    }
}

@Composable
fun AppContainer() {
    // 1. INICIALIZACIÓN ÚNICA
    // Se declaran una sola vez para que se compartan en toda la app.
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()
    val usuarioViewModel: UsuarioViewModel = viewModel() // ViewModel para Inicio y Registro
    val estadoViewModel: EstadoViewModel = viewModel()

    // 2. LISTENER DE EVENTOS DE NAVEGACIÓN
    // Este código escucha cuando llamas a viewModel.navigateTo(...) y ejecuta la navegación.
    LaunchedEffect(key1 = Unit) {
        mainViewModel.navigationEvents.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> {
                    navController.navigate(event.route.route) {
                        event.popUpToRoute?.let {
                            popUpTo(it.route) { inclusive = event.inclusive }
                        }
                        launchSingleTop = event.singleTop
                        restoreState = true
                    }
                }
                is NavigationEvent.PopBackStack -> navController.popBackStack()
                is NavigationEvent.NavigateUp -> navController.navigateUp()
            }
        }
    }

    // 3. ESTRUCTURA VISUAL PRINCIPAL
    // Un solo Scaffold y un solo NavHost para toda la app.
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            // La app comenzará en la pantalla de Home.
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // --- TODAS LAS PANTALLAS (RUTAS) DE TU APP SE DEFINEN AQUÍ ---

            composable(route = Screen.Home.route) {
                HomeScreen(navController = navController, viewModel = mainViewModel)
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen(navController = navController, viewModel = mainViewModel)
            }
            composable(route = Screen.Settings.route) {
                SettingsScreen(navController = navController, viewModel = mainViewModel)
            }
            composable(route = Screen.Inicio.route) {
                InicioScreen(
                    navController = navController,
                    viewModel = usuarioViewModel
                )
            }

            composable(route = Screen.Registro.route) {
                RegistroScreen(
                    navController = navController
                    // <-- Ya no se pasa el viewModel. La pantalla lo obtiene sola.
                )
            }

            // Nota: La ruta "PantallaPrincipal" debe ser definida en tu clase Screen.kt si planeas usarla.
            // Por ejemplo: data object PantallaPrincipal : Screen("pantalla_principal")
            // composable(route = Screen.PantallaPrincipal.route) {
            //     PantallaPrincial(
            //         modifier = Modifier,
            //         navController = navController,
            //         viewModel = estadoViewModel
            //     )
            // }
        }
    }
}

// Preview para la pantalla de inicio, que es la que se muestra al arrancar.
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TiendaBonbinTheme {
        val navController = rememberNavController()
        HomeScreen(navController = navController)
    }
}
