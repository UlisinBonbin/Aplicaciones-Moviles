package com.example.tienda_bonbin.data


import com.example.tienda_bonbin.data.model.CarritoItem
import com.example.tienda_bonbin.data.model.Compra
import com.example.tienda_bonbin.data.model.Producto
import com.example.tienda_bonbin.data.model.Usuario
import com.example.tienda_bonbin.data.model.dto.CarritoRequest
import com.example.tienda_bonbin.data.model.dto.CompraRequest
import com.example.tienda_bonbin.data.model.dto.LoginRequest
import com.example.tienda_bonbin.data.model.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/v1/usuarios")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>

    @POST("api/v1/usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("api/v1/productos")
    suspend fun getProductos(): List<Producto>

    // --- Endpoints de Carrito ---

    @GET("api/v1/carrito/{usuarioId}")
    suspend fun getCarritoByUsuarioId(@Path("usuarioId") usuarioId: Long): Response<List<CarritoItem>>

    @POST("api/v1/carrito")
    suspend fun agregarItemAlCarrito(@Body request: CarritoRequest): Response<CarritoItem>

    @DELETE("api/v1/carrito/{itemId}")
    suspend fun eliminarItemDelCarrito(@Path("itemId") itemId: Long): Response<Void>

    @DELETE("api/v1/carrito/limpiar/{usuarioId}")
    suspend fun limpiarCarrito(@Path("usuarioId") usuarioId: Long): Response<Void>

    // --- Endpoint de compra ---
    @POST("api/v1/compras")
    suspend fun registrarCompra(@Body request: CompraRequest): Response<Compra>
}
