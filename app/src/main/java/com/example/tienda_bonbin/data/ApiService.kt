package com.example.tienda_bonbin.data

// --- IMPORTACIONES CORREGIDAS Y COMPLETAS ---
// ✅ Se añaden explícitamente las clases del paquete 'model' que se usan en la interfaz.
import com.example.tienda_bonbin.data.model.CarritoItem
import com.example.tienda_bonbin.data.model.Compra
import com.example.tienda_bonbin.data.model.Producto
import com.example.tienda_bonbin.data.model.Usuario
import com.example.tienda_bonbin.data.model.dto.CarritoRequest
import com.example.tienda_bonbin.data.model.dto.CompraRequest
import com.example.tienda_bonbin.data.model.dto.LoginRequest
import retrofit2.Response
import retrofit2.http.*

/**
 * Interfaz que define todos los endpoints de la API para Retrofit.
 */
interface ApiService {

    @POST("api/v1/usuarios")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>

    @POST("api/v1/usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<Usuario>

    @GET("api/v1/productos")
    suspend fun getProductos(): List<Producto>

    // --- Endpoints de Carrito ---

    /**
     * Obtiene el carrito de un usuario.
     * ✅ Al tener el import correcto arriba, Kotlin ahora sabe que este 'CarritoItem'
     * es 'com.example.tienda_bonbin.data.model.CarritoItem'.
     */
    @GET("api/v1/carrito/{usuarioId}")
    suspend fun getCarritoByUsuarioId(@Path("usuarioId") usuarioId: Long): Response<List<CarritoItem>>

    @POST("api/v1/carrito")
    suspend fun agregarItemAlCarrito(@Body request: CarritoRequest): Response<CarritoItem>

    @DELETE("api/v1/carrito/{itemId}")
    suspend fun eliminarItemDelCarrito(@Path("itemId") itemId: Long): Response<Void>

    @DELETE("api/v1/carrito/limpiar/{usuarioId}")
    suspend fun limpiarCarrito(@Path("usuarioId") usuarioId: Long): Response<Void>

    // --- Endpoints de Compra ---

    @POST("api/v1/compras")
    suspend fun registrarCompra(@Body request: CompraRequest): Response<Compra>
}
