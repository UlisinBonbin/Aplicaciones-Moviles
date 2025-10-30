package com.example.tienda_bonbin.data

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "carrito_items",
    // La clave primaria será una combinación del usuario y el producto.
    // Esto asegura que un usuario solo puede tener una fila para cada producto único en su carrito.
    // Si quiere más, se actualizará la 'cantidad'.
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
    val usuarioId: Int, // ¿A qué usuario pertenece este item?
    val productoId: Int, // ¿Qué producto es?
    var cantidad: Int    // ¿Cuántas unidades de ese producto?
)
