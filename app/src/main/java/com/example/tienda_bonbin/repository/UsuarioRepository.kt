package com.example.tienda_bonbin.repository

import com.example.tienda_bonbin.data.ApiService
import com.example.tienda_bonbin.data.UsuarioDao
import com.example.tienda_bonbin.data.Usuario // Modelo de la Base de Datos (Room)
import com.example.tienda_bonbin.data.model.Usuario as NetworkUsuario // Modelo de la Red (Retrofit)

class UsuarioRepository(
    private val usuarioDao: UsuarioDao,
    private val apiService: ApiService
) {

    suspend fun registrarUsuario(networkUsuario: NetworkUsuario): Result<Unit> {
        return try {
            val response = apiService.registrarUsuario(networkUsuario)
            if (response.isSuccessful && response.body() != null) {
                val usuarioDesdeApi = response.body()!!

                // FIX: Construir el usuario para la DB manualmente
                // Usamos la contraseña de la petición original, ya que la respuesta no la trae
                val usuarioParaDb = Usuario(
                    id = usuarioDesdeApi.id?.toInt(),
                    nombre = usuarioDesdeApi.nombre,
                    apellido = usuarioDesdeApi.apellido,
                    correo = usuarioDesdeApi.correo,
                    clave = networkUsuario.contrasena, // <- Se guarda la contraseña original
                    direccion = usuarioDesdeApi.direccion,
                    rol = usuarioDesdeApi.rol
                )

                usuarioDao.insertarUsuario(usuarioParaDb)
                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error de registro en el servidor."
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualiza un usuario desde la red, pero preservando la contraseña local.
     */
    suspend fun actualizarUsuarioDesdeRed(networkUsuario: NetworkUsuario) {
        val userId = networkUsuario.id?.toInt() ?: return

        // 1. Obtener el usuario actual de la DB para no perder la clave
        val usuarioLocal = usuarioDao.obtenerUsuarioPorId(userId)

        // 2. Construir el objeto para la DB, combinando los datos nuevos con la clave vieja
        val usuarioParaDb = Usuario(
            id = userId,
            nombre = networkUsuario.nombre,
            apellido = networkUsuario.apellido,
            correo = networkUsuario.correo,
            clave = usuarioLocal?.clave ?: "", // <- Se preserva la clave existente
            direccion = networkUsuario.direccion,
            rol = networkUsuario.rol
        )

        // 3. Insertar/reemplazar en la base de datos
        usuarioDao.insertarUsuario(usuarioParaDb)
    }

    suspend fun insertarUsuario(usuario: Usuario) {
        usuarioDao.insertarUsuario(usuario)
    }

    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario? {
        return usuarioDao.obtenerUsuarioPorCorreo(correo)
    }

    suspend fun obtenerUsuarioPorId(usuarioId: Int): Usuario? = usuarioDao.obtenerUsuarioPorId(usuarioId)
}
