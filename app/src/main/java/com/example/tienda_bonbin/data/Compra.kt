package com.example.tienda_bonbin.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "compras",

    foreignKeys = [
        ForeignKey(
            entity = Usuario::class, // Se relaciona con la clase Usuario
            parentColumns = ["id"],  // Específicamente con la columna 'id' de la tabla 'usuarios'
            childColumns = ["usuarioId"], // A través de la columna 'usuarioId' de esta tabla
            onDelete = ForeignKey.CASCADE // Si se borra un usuario, se borran sus compras
        )
    ]
)
data class Compra(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    val fechaCompra: String,
    val total: Double
)
