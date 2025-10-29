package com.example.tienda_bonbin.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
// --- 1. SE CORRIGE LA LÍNEA DE IMPORTACIÓN ROTA ---
import com.example.tienda_bonbin.BonbinApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Inicializador para RegistroViewModel (sin cambios)
        initializer {
            RegistroViewModel(
                bonbinApplication().container.usuarioRepository
            )
        }

        // Inicializador para LoginViewModel (sin cambios)
        initializer {
            LoginViewModel(
                usuarioRepository = bonbinApplication().container.usuarioRepository,
                sessionRepository = bonbinApplication().container.sessionRepository
            )
        }

        // Inicializador para ProfileViewModel (CORREGIDO)
        initializer {
            // Ahora se inyectan los dos repositorios que necesita el nuevo ProfileViewModel.
            ProfileViewModel(
                usuarioRepository = bonbinApplication().container.usuarioRepository,
                sessionRepository = bonbinApplication().container.sessionRepository
            )
        }
    }
}

/**
 * Función de extensión para obtener la instancia de la aplicación
 * y poder acceder al contenedor de dependencias de forma segura. (sin cambios)
 */
fun CreationExtras.bonbinApplication(): BonbinApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BonbinApplication)
