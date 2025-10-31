package com.example.tienda_bonbin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface CompraDao {
    /**
     * Inserta una nueva compra en la tabla 'compras' y devuelve el ID
     * que se le ha asignado automáticamente.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarCompra(compra: Compra): Long // Devuelve el nuevo ID de la compra

    /**
     * Inserta una lista de detalles de compra en la tabla 'detalles_compra'.
     * Esto se usará para guardar todos los productos de una compra.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarDetalles(detalles: List<DetalleCompra>)
}