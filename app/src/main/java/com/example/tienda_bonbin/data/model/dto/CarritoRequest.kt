package com.example.tienda_bonbin.data.model.dto

data class CarritoRequest( val usuario: UsuarioId,
                           val producto: ProductoId,
                           val cantidad: Int)
data class UsuarioId(
    val id: Long
)

data class ProductoId(
    val id: Long
)