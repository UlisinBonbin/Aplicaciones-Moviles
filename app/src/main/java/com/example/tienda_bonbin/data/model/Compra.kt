package com.example.tienda_bonbin.data.model

data class Compra(val id: Long,
                  val usuario: Usuario, // Objeto Usuario anidado
                  val fechaCompra: String,
                  val detalles: List<DetalleCompra>)

data class DetalleCompra(
    val id: Long,
    val producto: Producto, // El objeto Producto completo anidado
    val cantidad: Int,
    val precioUnitario: Double
)