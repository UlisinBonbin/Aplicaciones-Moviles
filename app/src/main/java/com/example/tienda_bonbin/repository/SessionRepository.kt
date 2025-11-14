package com.example.tienda_bonbin.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio para gestionar los datos de la sesión del usuario (p. ej., el ID del usuario logueado).
 * Utiliza DataStore para persistir estos datos.
 * @param dataStore La instancia de DataStore que se usará para guardar las preferencias.
 */
class SessionRepository(private val dataStore: DataStore<Preferences>) { // <-- Acepta DataStore, no Context

    // ✅ 1. AÑADIMOS LAS NUEVAS CLAVES PARA EL TOKEN Y EL ROL
    private object PreferencesKeys {
        val USER_ID = intPreferencesKey("user_id")
        val AUTH_TOKEN = stringPreferencesKey("auth_token") // <-- Nueva clave para el token
        val USER_ROLE = stringPreferencesKey("user_role")   // <-- Nueva clave para el rol
    }

    // ✅ 2. AÑADIMOS FLOWS PARA OBSERVAR EL TOKEN Y EL ROL

    /**
     * Un Flow que emite el token de autenticación guardado cada vez que cambia.
     */
    val authTokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.AUTH_TOKEN]
    }

    /**
     * Un Flow que emite el rol del usuario guardado cada vez que cambia.
     */
    val userRoleFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_ROLE]
    }

    /**
     * Un Flow que emite el ID del usuario guardado. No cambia.
     */
    val userIdFlow: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_ID]
    }


    // ✅ 3. CREAMOS UNA NUEVA FUNCIÓN PARA GUARDAR LA SESIÓN COMPLETA

    /**
     * Guarda el ID del usuario, el token de autenticación y el rol en DataStore.
     */
    suspend fun saveSession(userId: Int, token: String, role: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
            preferences[PreferencesKeys.AUTH_TOKEN] = token
            preferences[PreferencesKeys.USER_ROLE] = role
        }
    }


    // ✅ 4. MEJORAMOS LA FUNCIÓN DE BORRADO PARA LIMPIAR TODO

    /**
     * Borra todos los datos de la sesión de DataStore, efectivamente cerrando la sesión.
     */
    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear() // Limpia todas las claves: userId, token y rol
        }
    }

    // ❌ Las funciones antiguas `saveUserId` y `clearUserId` ya no son necesarias,
    // ya que `saveSession` y `clearSession` son más completas. Puedes borrarlas si quieres.
    // Las dejo comentadas por si las quieres mantener por retrocompatibilidad.
    /*
    suspend fun saveUserId(userId: Int) { ... }
    suspend fun clearUserId() { ... }
    */
}
