package com.example.tienda_bonbin.data

data class Compra(
    val id: Int = 0,
    val usuarioId: Int,
    val productoId: Int,
    val fechaCompra: String
)
