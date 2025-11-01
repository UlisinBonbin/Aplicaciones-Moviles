package com.example.tienda_bonbin.data

import androidx.room.Entity
import androidx.room.ForeignKey



@Entity(
    tableName = "detalle_compra",
    primaryKeys = ["compraId", "productoId"],

    foreignKeys = [
        ForeignKey(
            entity = Compra::class,
            parentColumns = ["id"], // El 'id' de la tabla 'Compra'
            childColumns = ["compraId"],  // Se relaciona con 'compraId' en esta tabla
            onDelete = ForeignKey.CASCADE // Si se borra una Compra, se borran sus detalles.
        ),
        ForeignKey(
            entity = Producto::class,
            parentColumns = ["id"], // El 'id' de la tabla 'Producto'
            childColumns = ["productoId"], // Se relaciona con 'productoId' en esta tabla
            onDelete = ForeignKey.RESTRICT // Evita borrar un producto del catálogo si ya está en una compra.
        )
    ]
)



data class DetalleCompra(

    val compraId: Int,

    val productoId: Int,

    val cantidad: Int,

    val precioUnitario: Double
)
