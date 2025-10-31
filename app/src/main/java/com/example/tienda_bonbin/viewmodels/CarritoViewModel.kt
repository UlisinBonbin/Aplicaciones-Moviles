package com.example.tienda_bonbin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tienda_bonbin.data.CarritoItemInfo
import com.example.tienda_bonbin.repository.CarritoRepository
// --- 1. IMPORTACIONES AÑADIDAS ---
import com.example.tienda_bonbin.repository.CompraRepository
import com.example.tienda_bonbin.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// --- 2. ESTADO DE LA UI ACTUALIZADO ---
// Se añade el campo para mostrar mensajes al usuario (Snackbars)
data class CarritoUiState(
    val items: List<CarritoItemInfo> = emptyList(),
    val total: Double = 0.0,
    val mensajeUsuario: String? = null // Para los mensajes de "Compra finalizada", "Carrito vaciado", etc.
)

// --- 3. CONSTRUCTOR DEL VIEWMODEL ACTUALIZADO ---
// Ahora sí pide el CompraRepository, que es lo que espera el AppViewModelProvider.
class CarritoViewModel(
    private val carritoRepository: CarritoRepository,
    private val compraRepository: CompraRepository, // <-- AÑADIDO
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarritoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarItemsDelCarrito()
    }

    private fun cargarItemsDelCarrito() {
        viewModelScope.launch {
            val userId = sessionRepository.userIdFlow.first()
            if (userId != null) {
                carritoRepository.obtenerItemsDelCarrito(userId).collect { items ->
                    val totalCalculado = items.sumOf { it.precio * it.cantidad }
                    // Se usa .update para modificar el estado de forma segura
                    _uiState.update { it.copy(items = items, total = totalCalculado) }
                }
            }
        }
    }

    // --- 4. NUEVAS FUNCIONES AÑADIDAS ---

    /**
     * Llama al repositorio para vaciar el carrito del usuario actual
     * y muestra un mensaje de confirmación.
     */
    fun vaciarCarrito() {
        viewModelScope.launch {
            val userId = sessionRepository.userIdFlow.first()
            if (userId != null) {
                carritoRepository.vaciarCarrito(userId)
                _uiState.update { it.copy(mensajeUsuario = "El carrito ha sido vaciado") }
            }
        }
    }

    /**
     * Orquesta el proceso de finalizar una compra:
     * 1. Llama al CompraRepository para crear la compra y sus detalles.
     * 2. Llama al CarritoRepository para vaciar el carrito.
     * 3. Muestra un mensaje de éxito.
     */
    fun finalizarCompra() {
        viewModelScope.launch {
            val userId = sessionRepository.userIdFlow.first()
            // Asegurarnos de que hay un usuario y de que hay items en el carrito
            if (userId != null && _uiState.value.items.isNotEmpty()) {
                compraRepository.crearCompraDesdeCarrito(userId, _uiState.value.items)
                carritoRepository.vaciarCarrito(userId) // Se vacía después de confirmar la compra
                _uiState.update { it.copy(mensajeUsuario = "¡Gracias por tu compra!") }
            }
        }
    }

    /**
     * Resetea el mensaje del usuario después de que ha sido mostrado en la UI.
     * Esto evita que el Snackbar aparezca de nuevo si la pantalla se recompone.
     */
    fun mensajeMostrado() {
        _uiState.update { it.copy(mensajeUsuario = null) }
    }
}
