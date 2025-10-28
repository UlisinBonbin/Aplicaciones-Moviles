package com.example.tienda_bonbin.di
import android.content.Context
import com.example.tienda_bonbin.data.AppDatabase
import com.example.tienda_bonbin.repository.UsuarioRepository

/**
 * Contenedor de dependencias para toda la aplicación.
 */
interface AppContainer {
    val usuarioRepository: UsuarioRepository
}

/**
 * Implementación del contenedor que crea las instancias.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    // El repositorio se crea una sola vez (lazy) y se reutiliza.
    override val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepository(AppDatabase.getDatabase(context).usuarioDao())
    }
}
