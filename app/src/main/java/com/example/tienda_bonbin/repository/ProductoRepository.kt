package com.example.tienda_bonbin.repository

import com.example.tienda_bonbin.data.ApiService
// ✅ 1. Importamos el modelo de la API, no el de Room.
import com.example.tienda_bonbin.data.model.Producto

/**
 * Repositorio para obtener los productos desde la API.
 * ✅ 2. Su constructor ahora está vacío (ya no usa ProductoDao).
 */
class ProductoRepository(private val apiService: ApiService) {

    // Obtiene la instancia de ApiService para hacer llamadas de red.

    /**
     * ✅ 3. ESTA ES LA FUNCIÓN QUE TU VIEWMODEL ESTÁ BUSCANDO.
     * Llama a la API para obtener la lista de todos los productos.
     */
    suspend fun getProductos(): List<Producto> {
        // Simplemente llama al método correspondiente en ApiService.
        return apiService.getProductos()
    }
}
