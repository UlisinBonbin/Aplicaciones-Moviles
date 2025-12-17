package com.example.tienda_bonbin.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SessionRepository(private val dataStore: DataStore<Preferences>) {


    private object PreferencesKeys {
        val USER_ID = intPreferencesKey("user_id")
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val USER_ROLE = stringPreferencesKey("user_role")
    }


    val authTokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.AUTH_TOKEN]
    }

    val userRoleFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_ROLE]
    }

    val userIdFlow: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_ID]
    }

    suspend fun saveSession(userId: Int, token: String, role: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userId
            preferences[PreferencesKeys.AUTH_TOKEN] = token
            preferences[PreferencesKeys.USER_ROLE] = role
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear() // Limpia todas las claves: userId, token y rol
        }
    }

}
