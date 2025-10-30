package com.example.tienda_bonbin.data

data class CarritoItemInfo(
    val productoId: Int,
    val nombre: String,
    val precio: Double,
    val imagenUrl: String,
    val cantidad: Int
)
