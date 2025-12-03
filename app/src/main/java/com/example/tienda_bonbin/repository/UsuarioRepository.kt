package com.example.tienda_bonbin.repository

import com.example.tienda_bonbin.data.ApiService
import com.example.tienda_bonbin.data.UsuarioDao
import com.example.tienda_bonbin.data.Usuario // Modelo de la Base de Datos (Room)
import com.example.tienda_bonbin.data.model.Usuario as NetworkUsuario // Modelo de la Red (Retrofit)

/**
 * Repositorio que centraliza la lógica de datos del usuario, tanto local como remota.
 *
 * @param usuarioDao El DAO para acceder a la base de datos local.
 * @param apiService El servicio de Retrofit para acceder al backend.
 */
class UsuarioRepository(
    private val usuarioDao: UsuarioDao,
    private val apiService: ApiService
) {

    /**
     * Registra un nuevo usuario. Primero en el backend y luego, si es exitoso,
     * en la base de datos local.
     *
     * @param networkUsuario El objeto de usuario (modelo de red) a registrar.
     * @return Un objeto [Result] que encapsula el éxito o el fracaso de la operación.
     */
    suspend fun registrarUsuario(networkUsuario: NetworkUsuario): Result<Unit> {
        return try {
            val response = apiService.registrarUsuario(networkUsuario)
            if (response.isSuccessful && response.body() != null) {
                // Éxito en el backend: Mapear la respuesta y guardarla localmente.
                val usuarioDesdeApi = response.body()!!
                val usuarioParaDb = usuarioDesdeApi.toDbModel()
                usuarioDao.insertarUsuario(usuarioParaDb)
                Result.success(Unit)
            } else {
                // Error del servidor (ej. 4xx, 5xx).
                val errorMsg = response.errorBody()?.string() ?: "Error de registro en el servidor."
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            // Error de red (sin conexión, etc.) o cualquier otra excepción.
            Result.failure(e)
        }
    }

    suspend fun insertarUsuario(usuario: Usuario) {
        usuarioDao.insertarUsuario(usuario)
    }

    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario? {
        return usuarioDao.obtenerUsuarioPorCorreo(correo)
    }

    suspend fun obtenerUsuarioPorId(usuarioId: Int): Usuario? = usuarioDao.obtenerUsuarioPorId(usuarioId)
}

/**
 * Función de extensión para mapear un `NetworkUsuario` (de la API)
 * a un `Usuario` (para la base de datos local).
 */
private fun NetworkUsuario.toDbModel(): Usuario {
    require(this.id != null) { "El ID del usuario de la API no puede ser nulo." }
    return Usuario(
        id = this.id.toInt(), // Convertimos el Long de la API a Int para Room
        nombre = this.nombre,
        apellido = this.apellido,
        correo = this.correo,
        // IMPORTANTE: No guardamos la contraseña en texto plano en la DB local.
        clave = "",
        direccion = this.direccion,
        rol = this.rol
    )
}
