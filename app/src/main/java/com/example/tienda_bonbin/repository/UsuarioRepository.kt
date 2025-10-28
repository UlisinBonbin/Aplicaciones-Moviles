package com.example.tienda_bonbin.repository

import com.example.tienda_bonbin.data.Usuario
import com.example.tienda_bonbin.data.UsuarioDao

/**
 * Repositorio que maneja las operaciones de datos para la entidad Usuario.
 * Este es el intermediario entre los ViewModels y las fuentes de datos (en este caso, el DAO de Room).
 *
 * @param usuarioDao El Objeto de Acceso a Datos para los usuarios, que el repositorio usará para
 * interactuar con la base de datos.
 */
class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    /**
     * Llama a la función del DAO para insertar un nuevo usuario en la base de datos.
     * Esta función es 'suspend' porque el DAO la define así.
     */
    suspend fun insertarUsuario(usuario: Usuario) {
        usuarioDao.insertarUsuario(usuario)
    }

    /**
     * Llama a la función del DAO para obtener un usuario por su correo.
     * Esta función también es 'suspend'.
     */
    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario? {
        return usuarioDao.obtenerUsuarioPorCorreo(correo)
    }
}
