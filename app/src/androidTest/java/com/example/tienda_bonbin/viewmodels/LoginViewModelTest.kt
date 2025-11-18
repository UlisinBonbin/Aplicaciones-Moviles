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

// Se define una instancia de DataStore específica para el test para evitar conflictos.
private const val TEST_DATASTORE_NAME = "test_datastore"
private val Context.testDataStore by preferencesDataStore(name = TEST_DATASTORE_NAME)

/**
 * Test de instrumentación para LoginViewModel.
 *
 * Este test se ejecuta en un dispositivo o emulador Android.
 * Se enfoca en verificar la lógica del ViewModel cuando los campos están vacíos,
 * utilizando instancias reales de los repositorios y una base de datos de prueba en memoria.
 */
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var db: AppDatabase
    private lateinit var context: Context

    @Before
    fun setUp() {
        // 1.Se obtiene el contexto de la aplicación bajo prueba.
        context = ApplicationProvider.getApplicationContext<Context>()

        // 2. Creamos una base de datos EN MEMORIA específica para el test.
        // Esto asegura que cada prueba esté aislada y no afecte la base de datos real.
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        // 3. Instanciamos los repositorios con las dependencias creadas para el test.
        val usuarioRepository = UsuarioRepository(db.usuarioDao())

        // Se crea el SessionRepository usando la instancia de DataStore definida para este test.

        val sessionRepository = SessionRepository(context.testDataStore)

        val apiService = NetworkModule.getApiService(sessionRepository)
        // 4. Inicializamos el ViewModel con las dependencias.
        viewModel = LoginViewModel(usuarioRepository, sessionRepository, apiService)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        // Cerramos la base de datos después de cada prueba.
        db.close()
        // Limpiamos los datos del DataStore de prueba.
        runBlocking {
            context.testDataStore.edit { it.clear() }
        }
    }

    /**
     * Prueba para el fallo de inicio de sesión con campos vacíos.
     */
    @Test
    fun login_falla_por_campos_vacios() = runBlocking {
        // 1. ARRANGE (Preparar)
        viewModel.onLoginValueChange(correo = "  ", clave = "  ")

        // 2. ACT (Actuar)
        viewModel.iniciarSesion()

        // 3. ASSERT (Verificar)
        val estadoActual = viewModel.uiState.value

        assertNotNull("El mensaje de error no debería ser nulo", estadoActual.mensajeError)
        assertEquals("Correo y contraseña son obligatorios", estadoActual.mensajeError)
        assertFalse("El login no debería ser exitoso", estadoActual.loginExitoso)
    }
}