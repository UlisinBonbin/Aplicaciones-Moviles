package com.example.tienda_bonbin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tienda_bonbin.data.Usuario

// --- 1. IMPORTACIONES NUEVAS ---
import com.example.tienda_bonbin.repository.SessionRepository
import com.example.tienda_bonbin.repository.UsuarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// El data class `ProfileUiState` no cambia, está perfecto como está.
data class ProfileUiState(
    val usuario: Usuario? = null,
    val isLoading: Boolean = true,
    val sesionCerrada: Boolean = false
)

// --- 2. CAMBIO EN EL CONSTRUCTOR ---
// Ya no recibe `usuarioId`. Ahora pide el `SessionRepository` para ser dinámico.
class ProfileViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        // --- 3. NUEVA LÓGICA DE INICIALIZACIÓN ---
        // Se suscribe a los cambios del ID de usuario guardado en la sesión.
        viewModelScope.launch {
            // `userIdFlow` es un Flow. `collect` se ejecutará cada vez que el ID cambie
            // (cuando se inicia sesión o cuando se cierra la sesión).
            sessionRepository.userIdFlow.collect { usuarioId ->
                _uiState.update { it.copy(isLoading = true) }
                if (usuarioId != null) {
                    // Si hay un ID, buscamos al usuario correspondiente.
                    // Asegúrate de que el método en tu UsuarioRepository se llame `getUsuarioById`.
                    // Si lo llamaste `obtenerUsuarioPorId`, cámbialo aquí.
                    val usuario = usuarioRepository.obtenerUsuarioPorId(usuarioId)
                    _uiState.update { it.copy(usuario = usuario, isLoading = false) }
                } else {
                    // Si el ID es null, significa que no hay sesión. Limpiamos los datos.
                    _uiState.update { it.copy(usuario = null, isLoading = false) }
                }
            }
        }
    }

    // --- 4. NUEVA LÓGICA PARA CERRAR SESIÓN ---
    fun cerrarSesion() {
        viewModelScope.launch {
            // Le pedimos al SessionRepository que borre el ID guardado.
            sessionRepository.clearUserId()
            // El `collect` en el `init` detectará este cambio y pondrá el usuario a null.
            // Adicionalmente, marcamos `sesionCerrada` para que la UI pueda navegar.
            _uiState.update { it.copy(sesionCerrada = true) }
        }
    }
}
