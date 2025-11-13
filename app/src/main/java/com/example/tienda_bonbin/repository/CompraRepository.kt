package com.example.tienda_bonbin.repository

import com.example.tienda_bonbin.data.ApiService
import com.example.tienda_bonbin.data.NetworkModule
import com.example.tienda_bonbin.data.model.Compra
import com.example.tienda_bonbin.data.model.dto.CompraRequest
import retrofit2.Response

/**
 * Repositorio para gestionar las operaciones de Compra.
 * AHORA HABLA CON LA API, ya no usa CompraDao.
 * Su constructor está vacío.
 */
class CompraRepository {

    // Obtenemos la instancia de ApiService desde nuestro NetworkModule
    private val apiService: ApiService = NetworkModule.apiService

    /**
     * Llama a la API para registrar una nueva compra en el servidor.
     * ESTA ES LA FUNCIÓN QUE FALTABA.
     * @param request El objeto CompraRequest que contiene el ID del usuario.
     * @return La respuesta del servidor, que contiene la Compra creada.
     */
    suspend fun registrarCompra(request: CompraRequest): Response<Compra> {
        return apiService.registrarCompra(request)
    }
}
