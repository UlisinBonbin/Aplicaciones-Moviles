package com.example.tienda_bonbin.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
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
 * Las definiciones aquí no cambian.
 */
interface AppContainer {
    val usuarioRepository: UsuarioRepository
    val sessionRepository: SessionRepository
    val productoRepository: ProductoRepository
    val carritoRepository: CarritoRepository
    val compraRepository: CompraRepository
}

/**
 * Implementación del contenedor que crea las instancias.
 * AHORA CREA LOS REPOSITORIOS DE RED, QUE TIENEN CONSTRUCTORES VACÍOS.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    // ✅ CORRECCIÓN: Ahora se instancia sin parámetros (constructor vacío).
    override val productoRepository: ProductoRepository by lazy {
        ProductoRepository()
    }

    // ✅ CORRECCIÓN: Ahora se instancia sin parámetros (constructor vacío).
    override val carritoRepository: CarritoRepository by lazy {
        CarritoRepository()
    }

    // ✅ CORRECCIÓN: Ahora se instancia sin parámetros (constructor vacío).
    override val compraRepository: CompraRepository by lazy {
        CompraRepository()
    }

    // El UsuarioRepository todavía puede depender de Room si quieres mantener los datos
    // del usuario logueado de forma local, o lo podemos cambiar también.
    // Por ahora, lo dejamos como estaba para no romper el login.
    override val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepository(AppDatabase.getDatabase(context).usuarioDao())
    }

    // SessionRepository no cambia, siempre depende de DataStore.
    override val sessionRepository: SessionRepository by lazy {
        SessionRepository(context.dataStore)
    }
}
