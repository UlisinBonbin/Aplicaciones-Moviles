package com.example.tienda_bonbin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tienda_bonbin.data.Producto
import com.example.tienda_bonbin.repository.CarritoRepository
import com.example.tienda_bonbin.repository.ProductoRepository // <-- Importante
import com.example.tienda_bonbin.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CatalogoUiState(
    val productos: List<Producto> = emptyList(),
    val mensajeUsuario: String? = null
)

// El constructor ya está bien, acepta los 3 repositorios.
class CatalogoViewModel(
    private val sessionRepository: SessionRepository,
    private val carritoRepository: CarritoRepository,
    private val productoRepository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // --- 1. ¡CORRECCIÓN CLAVE! ---
        // Llama a la función que carga desde la base de datos.
        cargarProductosDesdeDb()
    }

    // --- 2. ELIMINAMOS LA LISTA ESTÁTICA ---
    // Esta función ahora usa el repositorio para obtener los productos reales de la BD.
    private fun cargarProductosDesdeDb() {
        viewModelScope.launch {
            productoRepository.obtenerTodosLosProductos().collect { productosDb ->
                _uiState.update { it.copy(productos = productosDb) }
            }
        }
    }

    // El resto de la clase ya está bien.
    fun agregarProductoAlCarrito(producto: Producto) {
        viewModelScope.launch {
            val userId = sessionRepository.userIdFlow.first()

            if (userId == null) {
                _uiState.update {
                    it.copy(mensajeUsuario = "Necesitas iniciar sesión para añadir productos")
                }
            } else {
                carritoRepository.agregarProducto(productoId = producto.id, usuarioId = userId)
                _uiState.update {
                    it.copy(mensajeUsuario = "${producto.nombre} añadido al carrito!")
                }
            }
        }
    }

    fun mensajeMostrado() {
        _uiState.update { it.copy(mensajeUsuario = null) }
    }
}
