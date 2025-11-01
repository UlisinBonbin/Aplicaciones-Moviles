package com.example.tienda_bonbin.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio para gestionar los datos de la sesión del usuario (p. ej., el ID del usuario logueado).
 * Utiliza DataStore para persistir estos datos.
 * @param dataStore La instancia de DataStore que se usará para guardar las preferencias.
 */
class SessionRepository(private val dataStore: DataStore<Preferences>) { // <-- Acepta DataStore, no Context

    // Define una clave para guardar el ID del usuario. Es como el "nombre" de la variable en DataStore.
    private object PreferencesKeys {
        val USER_ID = intPreferencesKey("user_id")
    }

    /**
     * Guarda el ID del usuario en DataStore.
     * La función `edit` es una transacción segura.
     */
    suspend fun saveUserId(userId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
        }
    }

    /**
     * Borra el ID del usuario de DataStore, efectivamente cerrando la sesión.
     */
    suspend fun clearUserId() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_ID)
        }
    }

    /**
     * Un Flow que emite el ID del usuario guardado cada vez que cambia.
     * Si no hay ID guardado (p. ej., nadie ha iniciado sesión), emite null.
     */
    val userIdFlow: Flow<Int?> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USER_ID]
        }
}
