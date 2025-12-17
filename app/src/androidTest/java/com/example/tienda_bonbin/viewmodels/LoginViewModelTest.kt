package com.example.tienda_bonbin.viewmodels

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.tienda_bonbin.data.AppDatabase
import com.example.tienda_bonbin.data.NetworkModule
import com.example.tienda_bonbin.repository.SessionRepository
import com.example.tienda_bonbin.repository.UsuarioRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

private const val TEST_DATASTORE_NAME = "test_datastore"
private val Context.testDataStore by preferencesDataStore(name = TEST_DATASTORE_NAME)

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var db: AppDatabase
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        // Crear dependencias
        val sessionRepository = SessionRepository(context.testDataStore)
        val apiService = NetworkModule.getApiService(sessionRepository)

        // Crear UsuarioRepository con todas sus dependencias
        val usuarioRepository = UsuarioRepository(db.usuarioDao(), apiService)

        // Crear el ViewModel con las suyas
        viewModel = LoginViewModel(usuarioRepository, sessionRepository, apiService)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
        runBlocking {
            context.testDataStore.edit { it.clear() }
        }
    }

    @Test
    fun login_falla_por_campos_vacios() = runBlocking {
        // Preparar
        viewModel.onLoginValueChange(correo = "  ", clave = "  ")

        // Actuar
        viewModel.iniciarSesion()

        // Verificar
        val estadoActual = viewModel.uiState.value

        assertNotNull("El mensaje de error no debería ser nulo", estadoActual.mensajeError)
        assertEquals("Correo y contraseña son obligatorios", estadoActual.mensajeError)
        assertFalse("El login no debería ser exitoso", estadoActual.loginExitoso)
    }
}
