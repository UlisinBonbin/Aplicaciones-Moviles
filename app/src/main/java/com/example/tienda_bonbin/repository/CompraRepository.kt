package com.example.tienda_bonbin.repository

import com.example.tienda_bonbin.data.ApiService
import com.example.tienda_bonbin.data.model.Compra
import com.example.tienda_bonbin.data.model.dto.CompraRequest
import retrofit2.Response

class CompraRepository(private val apiService: ApiService) {

    suspend fun registrarCompra(request: CompraRequest): Response<Compra> {
        return apiService.registrarCompra(request)
    }
}
