package com.example.tienda_bonbin.repository
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// --- CAMBIO AQUÍ: Renombramos la variable de extensión ---
private val Context.estadoDataStore by preferencesDataStore(name = "preferencias_usuario")

class EstadoDataStore(private val context: Context){
    private val ESTADO_ACTIVADO = booleanPreferencesKey("modo_activado")

    suspend fun guardarEstado(valor: Boolean){
        // --- Y la usamos aquí ---
        context.estadoDataStore.edit { preferencias ->
            preferencias[ESTADO_ACTIVADO] = valor
        }
    }

    fun  obtenerEstado(): Flow<Boolean> {
        // --- Y aquí ---
        return context.estadoDataStore.data.map { preferencias ->
            preferencias[ESTADO_ACTIVADO] ?: false
        }
    }
}
