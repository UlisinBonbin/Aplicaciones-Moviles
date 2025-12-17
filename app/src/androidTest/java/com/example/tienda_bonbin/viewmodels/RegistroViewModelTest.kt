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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

private const val TEST_DATASTORE_NAME = "test_datastore_registro"
private val Context.testDataStore by preferencesDataStore(name = TEST_DATASTORE_NAME)

@ExperimentalCoroutinesApi
class RegistroViewModelTest {

    private lateinit var viewModel: RegistroViewModel
    private lateinit var db: AppDatabase
    private lateinit var context: Context
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        // Crear dependencias
        val sessionRepository = SessionRepository(context.testDataStore)
        val apiService = NetworkModule.getApiService(sessionRepository)
        val usuarioRepository = UsuarioRepository(db.usuarioDao(), apiService)

        // Crear el ViewModel solo con las dependencias que necesita
        viewModel = RegistroViewModel(usuarioRepository)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
        runBlocking {
            context.testDataStore.edit { it.clear() }
        }
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `registro_con_capos_vacios`() {
        runBlocking {
            // Preparar
            viewModel.onRegistroValueChange(
                nombre = "",
                apellido = "",
                correo = "",
                clave = "",
                confirmarClave = ""
            )

            // Actuar
            viewModel.registrarUsuario()

            // Verificar
            val uiState = viewModel.uiState.value
            assertNotNull(uiState.mensajeError)
            assertEquals("Todos los campos son obligatorios", uiState.mensajeError)
            assertFalse(uiState.isLoading)
            assertFalse(uiState.registroExitoso)
        }
    }

    @Test
    fun `registrar_con_contrasennia_diferente`() {
        runBlocking {
            // Preparar
            viewModel.onRegistroValueChange(
                nombre = "Test",
                apellido = "User",
                correo = "test@example.com",
                clave = "password123",
                confirmarClave = "password456",
                direccion = "123 Test St",
                terminos = true
            )

            // Actuar
            viewModel.registrarUsuario()

            // Verificar
            val uiState = viewModel.uiState.value
            assertNotNull(uiState.mensajeError)
            assertEquals("Las contraseñas no coinciden", uiState.mensajeError)
            assertFalse(uiState.isLoading)
            assertFalse(uiState.registroExitoso)
        }
    }

}