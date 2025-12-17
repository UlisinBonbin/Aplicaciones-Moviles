package com.example.tienda_bonbin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tienda_bonbin.data.model.CarritoItem
import com.example.tienda_bonbin.data.model.dto.CompraRequest
import com.example.tienda_bonbin.repository.CarritoRepository
import com.example.tienda_bonbin.repository.CompraRepository
import com.example.tienda_bonbin.repository.SessionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


data class CarritoUiState(
    val items: List<CarritoItem> = emptyList(),
    val total: Double = 0.0,
    val mensajeUsuario: String? = null
)

class CarritoViewModel(
    private val carritoRepository: CarritoRepository,
    private val compraRepository: CompraRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarritoUiState())
    val uiState = _uiState.asStateFlow()

    private var userId: Int? = null

    init {
        viewModelScope.launch {
            userId = sessionRepository.userIdFlow.first()
            if (userId != null) {
                cargarItemsDelCarrito(userId!!)
            }
        }
    }


    private fun cargarItemsDelCarrito(usuarioId: Int) {
        viewModelScope.launch {
            carritoRepository.obtenerItemsDelCarrito(usuarioId).collect { itemsFromApi ->
                // El total se calcula a partir del precio del producto anidado.
                val totalCalculado = itemsFromApi.sumOf { it.producto.precio * it.cantidad }
                _uiState.update { currentState ->
                    currentState.copy(
                        items = itemsFromApi,
                        total = totalCalculado
                    )
                }
            }
        }
    }


    fun refrescarCarrito() {
        userId?.let { cargarItemsDelCarrito(it) }
    }


    fun eliminarItem(itemId: Long) {
        viewModelScope.launch {
            val response = carritoRepository.eliminarItemDelCarrito(itemId)
            if (response.isSuccessful) {
                refrescarCarrito() // Recargamos el carrito desde el servidor
            } else {
                _uiState.update { it.copy(mensajeUsuario = "Error al eliminar el producto") }
            }
        }
    }


    fun finalizarCompra() {
        viewModelScope.launch {
            if (userId != null && _uiState.value.items.isNotEmpty()) {
                val request = CompraRequest(usuarioId = userId!!.toLong())
                val response = compraRepository.registrarCompra(request)
                if(response.isSuccessful) {
                    // Si la compra es exitosa, el backend ya vació el carrito. Refrescamos la UI.
                    refrescarCarrito()
                    _uiState.update { it.copy(mensajeUsuario = "¡Gracias por tu compra!") }
                } else {
                    _uiState.update { it.copy(mensajeUsuario = "Error al procesar la compra") }
                }
            }
        }
    }

    fun mensajeMostrado() {
        _uiState.update { it.copy(mensajeUsuario = null) }
    }
}
