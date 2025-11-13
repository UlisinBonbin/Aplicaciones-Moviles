package com.example.tienda_bonbin.repository

// --- IMPORTACIONES CORREGIDAS ---
import com.example.tienda_bonbin.data.ApiService
// ✅ ESTA ES LA ÚNICA IMPORTACIÓN DE CarritoItem QUE DEBE EXISTIR EN ESTE ARCHIVO
import com.example.tienda_bonbin.data.model.CarritoItem
import com.example.tienda_bonbin.data.NetworkModule
import com.example.tienda_bonbin.data.model.dto.CarritoRequest
import com.example.tienda_bonbin.data.model.dto.ProductoId
import com.example.tienda_bonbin.data.model.dto.UsuarioId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

/**
 * Repositorio para gestionar las operaciones del carrito de compras.
 * Ahora habla DIRECTAMENTE con la API a través de Retrofit.
 * YA NO RECIBE CarritoDao EN EL CONSTRUCTOR.
 */
class CarritoRepository { // <-- Constructor vacío, ¡correcto!

    // Obtenemos la instancia de ApiService desde nuestro NetworkModule
    private val apiService: ApiService = NetworkModule.apiService

    /**
     * Obtiene la lista de items del carrito del usuario DESDE EL SERVIDOR.
     * El Flow ahora emite una lista del CarritoItem de la API.
     */
    fun obtenerItemsDelCarrito(usuarioId: Int): Flow<List<CarritoItem>> = flow {
        try {
            // Llamamos a la función de la API que definimos en ApiService
            val response = apiService.getCarritoByUsuarioId(usuarioId.toLong())
            if (response.isSuccessful) {
                // Si la respuesta es exitosa, emitimos la lista de items que viene en el cuerpo
                emit(response.body() ?: emptyList())
            } else {
                // Si el servidor da un error, emitimos una lista vacía
                println("Error al obtener el carrito: ${response.code()} - ${response.message()}")
                emit(emptyList())
            }
        } catch (e: Exception) {
            // En caso de error de red (sin conexión), también emitimos una lista vacía
            println("Excepción al obtener el carrito: ${e.message}")
            emit(emptyList())
        }
    }

    /**
     * Llama a la API para AGREGAR un producto al carrito en el servidor.
     */
    suspend fun agregarProductoAlCarrito(productoId: Int, usuarioId: Int) {
        val request = CarritoRequest(
            usuario = UsuarioId(usuarioId.toLong()),
            producto = ProductoId(productoId.toLong()),
            cantidad = 1
        )
        try {
            // Enviamos la petición
            apiService.agregarItemAlCarrito(request)
        } catch (e: Exception) {
            println("Excepción al agregar al carrito: ${e.message}")
        }
    }

    /**
     * Llama a la API para ELIMINAR un solo ítem del carrito en el servidor.
     */
    suspend fun eliminarItemDelCarrito(itemId: Long): Response<Void> {
        return apiService.eliminarItemDelCarrito(itemId)
    }

    /**
     * Llama a la API para VACIAR COMPLETAMENTE el carrito de un usuario en el servidor.
     */
    suspend fun limpiarCarrito(usuarioId: Int): Response<Void> {
        return apiService.limpiarCarrito(usuarioId.toLong())
    }
}
