package com.example.tienda_bonbin.data

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "carrito_items",
    primaryKeys = ["usuarioId", "productoId"],
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE // Si el usuario se elimina, su carrito también.
        ),
        ForeignKey(
            entity = Producto::class,
            parentColumns = ["id"],
            childColumns = ["productoId"],
            onDelete = ForeignKey.CASCADE // Si el producto se elimina del catálogo, se quita del carrito.
        )
    ]
)

data class CarritoItem(
    val usuarioId: Int,
    val productoId: Int,
    var cantidad: Int
)
