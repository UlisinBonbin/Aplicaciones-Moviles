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
import com.example.tienda_bonbin.navigation.NavigationEvent
import com.example.tienda_bonbin.navigation.Screen
import com.example.tienda_bonbin.ui.screen.CarritoScreen
import com.example.tienda_bonbin.ui.screen.CatalogoScreen
import com.example.tienda_bonbin.ui.screen.HomeScreen
import com.example.tienda_bonbin.ui.screen.LoginScreen
import com.example.tienda_bonbin.ui.screen.ProfileScreen
import com.example.tienda_bonbin.ui.screen.RegistroScreen
import com.example.tienda_bonbin.ui.theme.TiendaBonbinTheme
import com.example.tienda_bonbin.viewmodels.AppViewModelProvider
import com.example.tienda_bonbin.viewmodels.MainViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            TiendaBonbinTheme {
                AppContainer()
            }
        }
    }
}

@Composable
fun AppContainer() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()

    LaunchedEffect(key1 = Unit) {
        mainViewModel.navigationEvents.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> navController.navigate(event.route.route) {
                    event.popUpToRoute?.let { popUpTo(it.route) { inclusive = event.inclusive } }
                    launchSingleTop = event.singleTop
                    restoreState = true
                }
                is NavigationEvent.PopBackStack -> navController.popBackStack()
                is NavigationEvent.NavigateUp -> navController.navigateUp()
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Home.route) {
                HomeScreen(navController = navController, viewModel = mainViewModel)
            }

            composable(route = Screen.Profile.route) {
                ProfileScreen(
                    navController = navController,
                    viewModel = viewModel(factory = AppViewModelProvider.Factory)
                )
            }

            composable(route = Screen.Registro.route) {
                RegistroScreen(
                    navController = navController,
                    viewModel = viewModel(factory = AppViewModelProvider.Factory)
                )
            }

            composable(route = Screen.Login.route) {
                LoginScreen(
                    navController = navController,
                    viewModel = viewModel(factory = AppViewModelProvider.Factory)
                )
            }


            composable(route = Screen.Catalogo.route) {
                CatalogoScreen(navController = navController)
            }

            composable(route = Screen.Carrito.route) {
                CarritoScreen(navController = navController)
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TiendaBonbinTheme {
        val navController = rememberNavController()
        HomeScreen(navController = navController)
    }
}
