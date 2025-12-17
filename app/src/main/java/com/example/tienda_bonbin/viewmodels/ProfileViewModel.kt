package com.example.tienda_bonbin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tienda_bonbin.data.Usuario
import com.example.tienda_bonbin.repository.SessionRepository
import com.example.tienda_bonbin.repository.UsuarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProfileUiState(
    val usuario: Usuario? = null,
    val isLoading: Boolean = true,
    val sesionCerrada: Boolean = false
)

class ProfileViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            sessionRepository.userIdFlow.collect { usuarioId ->
                _uiState.update { it.copy(isLoading = true) }
                if (usuarioId != null) {
                    val usuario = usuarioRepository.obtenerUsuarioPorId(usuarioId)
                    _uiState.update { it.copy(usuario = usuario, isLoading = false) }
                } else {
                    _uiState.update { it.copy(usuario = null, isLoading = false) }
                }
            }
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            // Le pedimos al SessionRepository que borre el ID guardado.
            sessionRepository.clearSession()
            _uiState.update { it.copy(sesionCerrada = true) }
        }
    }
}
