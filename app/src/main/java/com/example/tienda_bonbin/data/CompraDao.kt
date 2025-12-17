package com.example.tienda_bonbin.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface CompraDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarCompra(compra: Compra): Long // Devuelve el nuevo id de la compra

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarDetalles(detalles: List<DetalleCompra>)
}