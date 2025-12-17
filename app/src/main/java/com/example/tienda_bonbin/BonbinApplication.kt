package com.example.tienda_bonbin

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.example.tienda_bonbin.di.AppContainer
import com.example.tienda_bonbin.di.DefaultAppContainer
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore

private const val SESSION_PREFERENCES_NAME = "session_prefs"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SESSION_PREFERENCES_NAME
)
class BonbinApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
