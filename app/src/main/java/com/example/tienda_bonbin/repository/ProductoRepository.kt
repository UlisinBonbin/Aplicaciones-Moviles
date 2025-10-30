package com.example.tienda_bonbin.repository

import com.example.tienda_bonbin.data.Producto
// --- 1. CORREGIR LA IMPORTACIÓN SI ES NECESARIO ---
// Asegúrate de que el DAO se importe desde la carpeta 'dao'.
import com.example.tienda_bonbin.data.ProductoDao
import kotlinx.coroutines.flow.Flow

// --- 2. AÑADIR EL PARÁMETRO AL CONSTRUCTOR ---
// Ahora la clase recibe un 'productoDao' y lo guarda como una propiedad privada.
class ProductoRepository(private val productoDao: ProductoDao) {

    /**
     * Devuelve un Flow con todos los productos de la base de datos.
     * Ahora 'productoDao' es una propiedad de la clase y se puede usar.
     */
    fun obtenerTodosLosProductos(): Flow<List<Producto>> = productoDao.obtenerTodos()

    /**
     * Inserta una lista de productos en la base de datos.
     */
    suspend fun insertarTodos(productos: List<Producto>) {
        productoDao.insertarTodos(productos)
    }
}
