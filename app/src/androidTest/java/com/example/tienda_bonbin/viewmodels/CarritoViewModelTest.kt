package com.example.tienda_bonbin.viewmodels
import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.test.core.app.ApplicationProvider
import com.example.tienda_bonbin.data.ApiService
import com.example.tienda_bonbin.data.model.CarritoItem
import com.example.tienda_bonbin.data.model.Compra
import com.example.tienda_bonbin.data.model.Usuario
import com.example.tienda_bonbin.data.model.dto.CarritoRequest
import com.example.tienda_bonbin.data.model.dto.CompraRequest
import com.example.tienda_bonbin.data.model.dto.LoginRequest
import com.example.tienda_bonbin.data.model.dto.LoginResponse
import com.example.tienda_bonbin.repository.CarritoRepository
import com.example.tienda_bonbin.repository.CompraRepository
import com.example.tienda_bonbin.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test
import retrofit2.Response
import java.io.File
import java.util.UUID

// Alias para resolver conflicto de nombres entre el modelo de API y el de DB
import com.example.tienda_bonbin.data.model.Producto as ProductoModel
import com.example.tienda_bonbin.data.Producto as ProductoEntity

@OptIn(ExperimentalCoroutinesApi::class)
class CarritoViewModelTest {
    // se crea un ApiService falso para los 2 test
    private val fakeApiService = object : ApiService {
        val fakeProductoEntity = ProductoEntity(id = 1, nombre = "Torta Test", precio = 1000.0, imagenUrl = "url")
        val fakeCarritoItem = CarritoItem(id = 10L, producto = fakeProductoEntity, cantidad = 2)
        val fakeUsuario = Usuario(1L, "User", "Test", "test@mail.com", "123456", "Calle Test", "CLIENTE")

        override suspend fun registrarUsuario(usuario: Usuario): Response<Usuario> = Response.success(usuario)
        override suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> =
            Response.success(LoginResponse(token = "fake-token", usuario = fakeUsuario))
        override suspend fun getProductos(): List<ProductoModel> = emptyList()
        override suspend fun getCarritoByUsuarioId(usuarioId: Long): Response<List<CarritoItem>> =
            Response.success(listOf(fakeCarritoItem))
        override suspend fun agregarItemAlCarrito(request: CarritoRequest): Response<CarritoItem> = Response.success(fakeCarritoItem)
        override suspend fun eliminarItemDelCarrito(itemId: Long): Response<Void> = Response.success(null)
        override suspend fun limpiarCarrito(usuarioId: Long): Response<Void> = Response.success(null)
        override suspend fun registrarCompra(request: CompraRequest): Response<Compra> =
            Response.success(Compra(999L, fakeUsuario, "2024-05-01", emptyList()))
    }

    //este test verifica que cuando el usuario vea lops productos el precio total esté bien calculado
    @Test
    fun carga_carrito_calcula_total_correctamente() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Crear DataStore único para este test, lo quye evita conflictos con otros tests, porque si se usa el mismo se escribe en el test 1 y en el test 2 se puede como sobreescribir y genera error
        val testDataStore = PreferenceDataStoreFactory.create(
            scope = TestScope(testDispatcher + Job()),
            produceFile = { File(context.cacheDir, "test_datastore_${UUID.randomUUID()}.preferences_pb") }
        )

        val sessionRepository = SessionRepository(testDataStore)
        val carritoRepository = CarritoRepository(fakeApiService)
        val compraRepository = CompraRepository(fakeApiService)

        sessionRepository.saveSession(userId = 1, token = "fake-token", role = "CLIENTE")

        val viewModel = CarritoViewModel(carritoRepository, compraRepository, sessionRepository)

        var intentos = 0
        while (viewModel.uiState.value.items.isEmpty() && intentos < 50) {
            delay(100)
            intentos++
        }

        val state = viewModel.uiState.value
        assertFalse("El carrito no deberia estar vacío", state.items.isEmpty())
        assertEquals("Torta Test", state.items[0].producto.nombre)
        assertEquals(2000.0, state.total, 0.01)

        Dispatchers.resetMain()
    }

    //este test verigica que al finalizar la compra se muestre un mensaje de exito, pasando por todo el flujo completode compra
    @Test
    fun finalizar_compra_muestra_mensaje_exito() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)
        val context = ApplicationProvider.getApplicationContext<Context>()

        val testDataStore = PreferenceDataStoreFactory.create(
            scope = TestScope(testDispatcher + Job()),
            produceFile = { File(context.cacheDir, "test_datastore_${UUID.randomUUID()}.preferences_pb") }
        )

        val sessionRepository = SessionRepository(testDataStore)
        val carritoRepository = CarritoRepository(fakeApiService)
        val compraRepository = CompraRepository(fakeApiService)

        sessionRepository.saveSession(userId = 1, token = "fake-token", role = "CLIENTE")

        val viewModel = CarritoViewModel(carritoRepository, compraRepository, sessionRepository)

        var intentosCarga = 0
        while (viewModel.uiState.value.items.isEmpty() && intentosCarga < 50) {
            delay(100)
            intentosCarga++
        }
        assertFalse("no se cargó el carrito inicial", viewModel.uiState.value.items.isEmpty())

        viewModel.finalizarCompra()

        var intentosMensaje = 0
        while (viewModel.uiState.value.mensajeUsuario == null && intentosMensaje < 50) {
            delay(100)
            intentosMensaje++
        }

        assertNotNull("El mensaje de usuario sigue siendo null", viewModel.uiState.value.mensajeUsuario)
        assertEquals("¡Gracias por tu compra!", viewModel.uiState.value.mensajeUsuario)

        Dispatchers.resetMain()
    }

}