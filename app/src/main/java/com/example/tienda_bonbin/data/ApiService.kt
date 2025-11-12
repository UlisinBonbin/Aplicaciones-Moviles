package com.example.tienda_bonbin.data
import com.example.tienda_bonbin.data.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    /**
     * Define un endpoint para registrar un nuevo usuario.
     * @POST especifica que es una petición HTTP POST.
     * La ruta se concatena a la BASE_URL.
     *
     * URL Final = BASE_URL + "api/v1/usuarios"
     * (Revisa tu `UsuarioController` en Spring para la ruta correcta)
     */
    @POST("api/v1/usuarios")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>

    data class LoginRequest(val correo: String, val contrasena: String)

    // En ApiService.kt
    @POST("api/v1/usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<Usuario> // Devuelve el Usuario si el login es correcto

}