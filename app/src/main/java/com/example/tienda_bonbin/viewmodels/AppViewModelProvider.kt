package com.example.tienda_bonbin.viewmodels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tienda_bonbin.BonbinApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {

        // Inicializador para RegistroViewModel
        initializer {
            RegistroViewModel(
                usuarioRepository = bonbinApplication().container.usuarioRepository

            )
        }

        // Inicializador para LoginViewModel
        initializer {
            LoginViewModel(
                usuarioRepository = bonbinApplication().container.usuarioRepository,
                sessionRepository = bonbinApplication().container.sessionRepository,
                apiService = bonbinApplication().container.apiService
            )
        }

        // Inicializador para ProfileViewModel
        initializer {
            ProfileViewModel(
                usuarioRepository = bonbinApplication().container.usuarioRepository,
                sessionRepository = bonbinApplication().container.sessionRepository
            )
        }

        // Inicializador de CatalogoViewModel
        initializer {
            CatalogoViewModel(
                sessionRepository = bonbinApplication().container.sessionRepository,
                carritoRepository = bonbinApplication().container.carritoRepository,
                productoRepository = bonbinApplication().container.productoRepository
            )
        }

        // Inicializador para CarritoViewModel
        initializer {
            CarritoViewModel(
                carritoRepository = bonbinApplication().container.carritoRepository,
                compraRepository = bonbinApplication().container.compraRepository,
                sessionRepository = bonbinApplication().container.sessionRepository
            )
        }
    }
}

fun CreationExtras.bonbinApplication(): BonbinApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BonbinApplication)

