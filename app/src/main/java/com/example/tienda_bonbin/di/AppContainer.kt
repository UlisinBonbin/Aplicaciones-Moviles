package com.example.tienda_bonbin.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.tienda_bonbin.data.AppDatabase
import com.example.tienda_bonbin.repository.CarritoRepository
import com.example.tienda_bonbin.repository.ProductoRepository // La importación ya está
import com.example.tienda_bonbin.repository.SessionRepository
import com.example.tienda_bonbin.repository.UsuarioRepository

private const val SESSION_DATA_NAME = "session_data"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SESSION_DATA_NAME
)

/**
 * Contenedor de dependencias para toda la aplicación.
 */
interface AppContainer {
    val usuarioRepository: UsuarioRepository
    val sessionRepository: SessionRepository
    val carritoRepository: CarritoRepository
    // --- ¡AÑADE ESTA LÍNEA! ---
    val productoRepository: ProductoRepository
}

/**
 * Implementación del contenedor que crea las instancias.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    override val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepository(AppDatabase.getDatabase(context).usuarioDao())
    }

    override val sessionRepository: SessionRepository by lazy {
        SessionRepository(context.dataStore)
    }

    override val carritoRepository: CarritoRepository by lazy {
        CarritoRepository(AppDatabase.getDatabase(context).carritoDao())
    }

    // Ahora esta línea es correcta porque está implementando algo de la interfaz
    override val productoRepository: ProductoRepository by lazy {
        ProductoRepository(AppDatabase.getDatabase(context).productoDao())
    }
}
