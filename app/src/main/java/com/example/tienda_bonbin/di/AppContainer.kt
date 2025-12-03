package com.example.tienda_bonbin.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.tienda_bonbin.data.ApiService
import com.example.tienda_bonbin.data.AppDatabase
import com.example.tienda_bonbin.repository.CarritoRepository
import com.example.tienda_bonbin.repository.CompraRepository
import com.example.tienda_bonbin.repository.ProductoRepository
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
    val apiService: ApiService
    val usuarioRepository: UsuarioRepository
    val sessionRepository: SessionRepository
    val productoRepository: ProductoRepository
    val carritoRepository: CarritoRepository
    val compraRepository: CompraRepository

    val dataStore: DataStore<Preferences>
}

/**
 * Implementación del contenedor que crea las instancias.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    override val sessionRepository: SessionRepository by lazy {
        SessionRepository(context.dataStore)
    }

    override val apiService: ApiService by lazy {
        com.example.tienda_bonbin.data.NetworkModule.getApiService(sessionRepository)
    }

    override val dataStore: DataStore<Preferences> by lazy {
        context.dataStore
    }

    override val productoRepository: ProductoRepository by lazy {
        ProductoRepository(apiService)
    }

    override val carritoRepository: CarritoRepository by lazy {
        CarritoRepository(apiService)
    }

    override val compraRepository: CompraRepository by lazy {
        CompraRepository(apiService)
    }

    /**
     * Ahora UsuarioRepository también depende de la red.
     * Le pasamos tanto el DAO local como el servicio de API remoto.
     */
    override val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepository(
            usuarioDao = AppDatabase.getDatabase(context).usuarioDao(),
            apiService = apiService
        )
    }

}
