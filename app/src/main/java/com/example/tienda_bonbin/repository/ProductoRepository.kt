package com.example.tienda_bonbin.repository

import com.example.tienda_bonbin.data.ApiService
import com.example.tienda_bonbin.data.model.Producto

class ProductoRepository(private val apiService: ApiService) {


    suspend fun getProductos(): List<Producto> {
        return apiService.getProductos()
    }
}
