package com.example.tienda_bonbin.di

import android.content.Context
// --- 1. IMPORTACIONES PARA DATASTORE ---
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.tienda_bonbin.data.AppDatabase
import com.example.tienda_bonbin.repository.SessionRepository
import com.example.tienda_bonbin.repository.UsuarioRepository

// --- 2. DEFINICIÓN DEL DATASTORE A NIVEL DE ARCHIVO ---
// Esto crea una instancia Singleton del DataStore asociada al Context de la app.
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
}

/**
 * Implementación del contenedor que crea las instancias.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {

    // El repositorio de Usuario se crea una sola vez (lazy) y se reutiliza.
    override val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepository(AppDatabase.getDatabase(context).usuarioDao())
    }

    // --- 3. ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
    // Ahora creamos el SessionRepository pasándole el DataStore que necesita.
    override val sessionRepository: SessionRepository by lazy {
        // En lugar de `context`, le pasamos `context.dataStore`
        SessionRepository(context.dataStore)
    }
}
