package com.example.tienda_bonbin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    // Inserta una lista de productos. Si ya existen, los ignora.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarTodos(productos: List<Producto>)

    // Obtiene todos los productos de la tabla.
    @Query("SELECT * FROM productos")
    fun obtenerTodos(): Flow<List<Producto>>
}