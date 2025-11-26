package com.example.tienda_bonbin.screen

import app.cash.turbine.test // Asegúrate de tener la dependencia de Turbine
import com.example.tienda_bonbin.navigation.NavigationEvent
import com.example.tienda_bonbin.navigation.Screen
import com.example.tienda_bonbin.rules.MainDispatcherRule
import com.example.tienda_bonbin.viewmodels.MainViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class HomeScreenTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `MainViewModel navega a catalogo correctamente`() = runTest {
        // ARRANGE
        val viewModel = MainViewModel()

        // ACT & ASSERT
        // Usamos 'test' de Turbine para escuchar el Flow de forma segura
        viewModel.navigationEvents.test {
            // Primero, actuamos: llamamos a la función que emite el evento
            viewModel.navigateTo(Screen.Catalogo)

            // Luego, verificamos: esperamos y consumimos el evento
            val eventoEmitido = awaitItem()

            // Finalmente, hacemos las aserciones sobre el evento recibido
            val eventoNavegacion = eventoEmitido as NavigationEvent.NavigateTo
            assertEquals(Screen.Catalogo.route, eventoNavegacion.route.route)

            // Opcional: aseguramos que no se emitieron más eventos
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `MainViewModel navega a perfil correctamente`() = runTest {
        val viewModel = MainViewModel()

        viewModel.navigationEvents.test {
            viewModel.navigateTo(Screen.Profile)

            val eventoEmitido = awaitItem()

            val eventoNavegacion = eventoEmitido as NavigationEvent.NavigateTo
            assertEquals(Screen.Profile.route, eventoNavegacion.route.route)

            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `MainViewModel navega a carrito correctamente`() = runTest {
        val viewModel = MainViewModel()

        viewModel.navigationEvents.test {
            viewModel.navigateTo(Screen.Carrito)

            val eventoEmitido = awaitItem()

            val eventoNavegacion = eventoEmitido as NavigationEvent.NavigateTo
            assertEquals(Screen.Carrito.route, eventoNavegacion.route.route)

            ensureAllEventsConsumed()
        }
    }
}
