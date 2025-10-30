package com.example.tienda_bonbin.data

import androidx.room.Entity
import androidx.room.ForeignKey


// 1. ANOTACIÓN @Entity
// Indica que esta clase es una tabla en la base de datos Room.
@Entity(
    tableName = "detalle_compra",
    // 2. CLAVE PRIMARIA COMPUESTA
    // Esto evita que puedas añadir el mismo producto dos veces en la misma compra.
    // La combinación de `compraId` y `productoId` debe ser única.
    primaryKeys = ["compraId", "productoId"],

    // 3. CLAVES FORÁNEAS (Foreign Keys)
    // Esto crea las relaciones y asegura la integridad de tus datos.
    foreignKeys = [
        ForeignKey(
            entity = Compra::class,
            parentColumns = ["id"], // El 'id' de la tabla 'Compra'
            childColumns = ["compraId"],  // Se relaciona con 'compraId' en esta tabla
            onDelete = ForeignKey.CASCADE // IMPORTANTE: Si borras una Compra, se borran sus detalles.
        ),
        ForeignKey(
            entity = Producto::class,
            parentColumns = ["id"], // El 'id' de la tabla 'Producto'
            childColumns = ["productoId"], // Se relaciona con 'productoId' en esta tabla
            onDelete = ForeignKey.RESTRICT // Evita que borres un producto del catálogo si ya está en una compra.
        )
    ]
)



data class DetalleCompra(

    val compraId: Int,

    // Para saber qué producto específico se compró.
    val productoId: Int,

    val cantidad: Int,

    // Guarda el precio del producto EN EL MOMENTO DE LA COMPRA.
    // Es crucial porque los precios en tu catálogo pueden cambiar en el futuro.
    val precioUnitario: Double
)
