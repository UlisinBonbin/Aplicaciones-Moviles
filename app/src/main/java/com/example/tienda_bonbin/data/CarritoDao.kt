package com.example.tienda_bonbin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {
    // Inserta un item en el carrito.
    // Si el item ya existe (mismo usuarioId y productoId), lo reemplaza.
    // Usaremos una lógica más avanzada en el repositorio para sumar cantidades.

    @Update
    suspend fun actualizar(item: CarritoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(item: CarritoItem)

    // Obtiene un item específico del carrito para un usuario.
    // Será útil para saber si debemos insertar uno nuevo o actualizar la cantidad.
    @Query("SELECT * FROM carrito_items WHERE usuarioId = :usuarioId AND productoId = :productoId")
    suspend fun obtenerItem(usuarioId: Int, productoId: Int): CarritoItem?

    @Query("DELETE FROM carrito_items WHERE usuarioId = :usuarioId")
    suspend fun vaciarCarrito(usuarioId: Int)

    @Query("""
        SELECT
            p.id as productoId,
            p.nombre,
            p.precio,
            p.imagenUrl,
            ci.cantidad
        FROM carrito_items AS ci
        INNER JOIN productos AS p ON ci.productoId = p.id
        WHERE ci.usuarioId = :usuarioId
    """)
    fun obtenerItemsInfoDelCarrito(usuarioId: Int): Flow<List<CarritoItemInfo>>
}

    // @Query("DELETE FROM carrito_items WHERE usuarioId = :usuarioId AND productoId = :productoId")
  // suspend fun eliminarItem(usuarioId: Int, productoId: Int)
