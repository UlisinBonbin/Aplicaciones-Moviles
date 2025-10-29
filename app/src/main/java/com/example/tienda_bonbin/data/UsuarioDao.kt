package com.example.tienda_bonbin.data
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface UsuarioDao {

    /**
     * Inserta un nuevo usuario en la tabla.
     * Si ya existe un usuario con la misma clave primaria, será reemplazado.
     * La función es 'suspend' porque las operaciones de base de datos deben
     * ejecutarse fuera del hilo principal para no bloquear la UI.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario)

    /**
     * Busca y devuelve un usuario por su dirección de correo electrónico.
     * Devuelve un 'Usuario' si lo encuentra, o 'null' si no existe ninguno con ese correo.
     * Esta función la usaremos más adelante para el login.
     */
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE id = :usuarioId LIMIT 1")
    suspend fun obtenerUsuarioPorId(usuarioId: Int): Usuario?

}