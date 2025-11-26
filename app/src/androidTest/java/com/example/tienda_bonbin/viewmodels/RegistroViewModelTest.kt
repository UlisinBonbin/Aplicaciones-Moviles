package com.example.tienda_bonbin.viewmodels

import org.junit.Before
import android.content.Context
import androidx.datastore.core.DataStore // Importa DataStore
import androidx.datastore.preferences.core.Preferences // Importa Preferences
import androidx.datastore.preferences.preferencesDataStore // Importa el delegado
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.tienda_bonbin.data.AppDatabase
import com.example.tienda_bonbin.data.NetworkModule
import com.example.tienda_bonbin.repository.SessionRepository
import com.example.tienda_bonbin.repository.UsuarioRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*

import org.junit.Test
import java.io.IOException

// Se define un DataStore de prueba para este archivo.
// El nombre debe ser único para evitar conflictos con otros tests.
private const val TEST_DATASTORE_NAME = "test_registro_datastore"
private val Context.testDataStore: DataStore<Preferences> by preferencesDataStore(name = TEST_DATASTORE_NAME)

class RegistroViewModelTest {

    private lateinit var viewModel: RegistroViewModel
    private lateinit var db: AppDatabase
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        // Declaramos usuarioRepository aquí.
        val usuarioRepository = UsuarioRepository(db.usuarioDao())

        val sessionRepository = SessionRepository(context.testDataStore)

        // 2. Creamos la instancia de ApiService para el test.
        val apiService = NetworkModule.getApiService(sessionRepository)

        // 3.Ahora 'usuarioRepository' existe y se puede usar.
        viewModel = RegistroViewModel(usuarioRepository, apiService)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun registro_falla_por_campos_vacios() = runBlocking {
        // 1. prepara
        viewModel.onRegistroValueChange(nombre = " ", apellido = " ", correo = " ", clave = " ", confirmarClave = " ", direccion = " ")

        // 2. actua
        viewModel.registrarUsuario()

        // 3. verifica
        val estadoActual = viewModel.uiState.value

        assertNotNull("El mensaje de error no debería ser nulo", estadoActual.mensajeError)
        assertEquals("Todos los campos son obligatorios", estadoActual.mensajeError)
        assertFalse("El registro no debería ser exitoso", estadoActual.registroExitoso)
    }

    @Test
    fun registro_falla_por_claves_no_coinciden() = runBlocking {
        viewModel.onRegistroValueChange(
            nombre = "Test",
            apellido = "User",
            correo = "test@user.com",
            clave = "password123",
            confirmarClave = "password456",
            direccion = "Calle Falsa 123",
            terminos = true
        )

        viewModel.registrarUsuario()

        val estadoActual = viewModel.uiState.value
        assertEquals("Las contraseñas no coinciden", estadoActual.mensajeError)
        assertFalse(estadoActual.registroExitoso)
    }

    @Test
    fun registro_falla_por_correo_invalido() = runBlocking {
        viewModel.onRegistroValueChange(
            nombre = "Test",
            apellido = "User",
            correo = "correo-invalido",
            clave = "password123",
            confirmarClave = "password123",
            direccion = "Calle Falsa 123",
            terminos = true
        )

        viewModel.registrarUsuario()

        val estadoActual = viewModel.uiState.value
        assertEquals("El formato del correo electrónico no es válido", estadoActual.mensajeError)
        assertFalse(estadoActual.registroExitoso)
    }

    @Test
    fun falla_por_no_aceptar_terminos() = runBlocking {
        viewModel.onRegistroValueChange(
            nombre = "Test",
            apellido = "User",
            correo = "ulises@gmail.com",
            clave = "password123",
            confirmarClave = "password123",
            direccion = "Calle Falsa 123",
            terminos = false
        )
        viewModel.registrarUsuario()
        val estadoActual = viewModel.uiState.value
        assertEquals("Debes aceptar los términos y condiciones", estadoActual.mensajeError)
        assertFalse(estadoActual.registroExitoso)
    }
}
