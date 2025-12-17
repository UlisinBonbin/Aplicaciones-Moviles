package com.example.tienda_bonbin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tienda_bonbin.data.model.Producto
import com.example.tienda_bonbin.repository.CarritoRepository
import com.example.tienda_bonbin.repository.ProductoRepository
import com.example.tienda_bonbin.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CatalogoUiState(
    val productos: List<Producto> = emptyList(),
    val mensajeUsuario: String? = null
)

class CatalogoViewModel(
    private val sessionRepository: SessionRepository,
    private val carritoRepository: CarritoRepository,
    private val productoRepository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CatalogoUiState())
    val uiState: StateFlow<CatalogoUiState> = _uiState.asStateFlow()

    init {
        cargarProductosDesdeApi()
    }


    private fun cargarProductosDesdeApi() {
        viewModelScope.launch {
            try {
                // Llama al repositorio para obtener la lista de productos de la red.
                val listaProductosApi = productoRepository.getProductos()
                _uiState.update { it.copy(productos = listaProductosApi) }
            } catch (e: Exception) {
                // En caso de error de red, se deja la lista vacía y mostramos un mensaje.
                _uiState.update { it.copy(mensajeUsuario = "Error al cargar productos: ${e.message}") }
            }
        }
    }


    fun agregarProductoAlCarrito(producto: Producto) {
        viewModelScope.launch {
            val userId = sessionRepository.userIdFlow.first()
            if (userId != null) {
                carritoRepository.agregarProductoAlCarrito(productoId = producto.id.toInt(), usuarioId = userId)
                _uiState.update { it.copy(mensajeUsuario = "'${producto.nombre}' añadido al carrito") }
            } else {
                _uiState.update { it.copy(mensajeUsuario = "Error: Inicia sesión para comprar") }
            }
        }
    }

    /**
     * Limpia el mensaje del usuario una vez ha sido mostrado.
     */
    fun mensajeMostrado() {
        _uiState.update { it.copy(mensajeUsuario = null) }
    }
}
