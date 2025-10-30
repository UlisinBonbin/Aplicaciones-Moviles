package com.example.tienda_bonbin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tienda_bonbin.data.CarritoItemInfo
import com.example.tienda_bonbin.repository.CarritoRepository
import com.example.tienda_bonbin.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Estado de la UI para la pantalla del carrito
data class CarritoUiState(
    val items: List<CarritoItemInfo> = emptyList(),
    val total: Double = 0.0
)

class CarritoViewModel(
    private val carritoRepository: CarritoRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarritoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarItemsDelCarrito()
    }

    private fun cargarItemsDelCarrito() {
        viewModelScope.launch {
            // 1. Obtener el ID del usuario actual
            val userId = sessionRepository.userIdFlow.first()
            if (userId != null) {
                // 2. Usar el repositorio para obtener el Flow de items
                carritoRepository.obtenerItemsDelCarrito(userId).collect { items ->
                    // 3. Cuando el Flow emita nuevos datos, calcular el total
                    val totalCalculado = items.sumOf { it.precio * it.cantidad }
                    // 4. Actualizar el estado de la UI
                    _uiState.value = CarritoUiState(items = items, total = totalCalculado)
                }
            }
        }
    }
}
