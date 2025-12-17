package com.example.tienda_bonbin.repository

import com.example.tienda_bonbin.data.ApiService
import com.example.tienda_bonbin.data.UsuarioDao
import com.example.tienda_bonbin.data.Usuario
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

    suspend fun actualizarUsuarioDesdeRed(networkUsuario: NetworkUsuario) {
        val userId = networkUsuario.id?.toInt() ?: return

        // Obtener el usuario actual de la DB para no perder la clave
        val usuarioLocal = usuarioDao.obtenerUsuarioPorId(userId)

        // Construir el objeto para la DB, combinando los datos nuevos con la clave vieja
        val usuarioParaDb = Usuario(
            id = userId,
            nombre = networkUsuario.nombre,
            apellido = networkUsuario.apellido,
            correo = networkUsuario.correo,
            clave = usuarioLocal?.clave ?: "",
            direccion = networkUsuario.direccion,
            rol = networkUsuario.rol
        )

        // Insertar/reemplazar en la base de datos
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
