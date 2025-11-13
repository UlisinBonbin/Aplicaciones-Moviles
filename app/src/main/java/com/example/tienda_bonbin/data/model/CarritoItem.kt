package com.example.tienda_bonbin.data.model

import com.example.tienda_bonbin.data.Producto

data class CarritoItem(val id: Long,
                       val cantidad: Int,
                       val producto: Producto
)