package com.example.tienda_bonbin.viewmodels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tienda_bonbin.BonbinApplication // <-- Importaremos esto ahora
import com.example.tienda_bonbin.repository.UsuarioRepository

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Inicializador para el RegistroViewModel
        initializer {
            RegistroViewModel(
                // Llama a la función que obtiene el repositorio de la aplicación
                bonbinApplication().container.usuarioRepository
            )
        }

        // --- AQUÍ AÑADIRÍAS MÁS INICIALIZADORES PARA OTROS VIEWMODELS ---
        // initializer {
        //     LoginViewModel(bonbinApplication().container.usuarioRepository)
        // }
    }
}

/**
 * Función de extensión para obtener la instancia de la aplicación
 * y poder acceder al contenedor de dependencias.
 */
fun CreationExtras.bonbinApplication(): BonbinApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BonbinApplication)

