package com.example.tienda_bonbin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tienda_bonbin.repository.SessionRepository
import com.example.tienda_bonbin.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Define la estructura de los datos que la pantalla de Login necesita
data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val loginExitoso: Boolean = false,
    val mensajeError: String? = null,
    val isLoading: Boolean = false // Para mostrar una rueda de carga mientras se valida
)

/**
 * ViewModel para la lógica de la pantalla de Login.
 *
 * @param usuarioRepository Repositorio para acceder a los datos de los usuarios (Room).
 * @param sessionRepository Repositorio para gestionar la sesión del usuario (DataStore).
 */
class LoginViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Se llama desde la UI cada vez que el usuario escribe en los campos de texto.
     */
    fun onLoginValueChange(correo: String? = null, clave: String? = null) {
        _uiState.update { currentState ->
            currentState.copy(
                correo = correo ?: currentState.correo,
                clave = clave ?: currentState.clave
            )
        }
    }

    /**
     * Orquesta el proceso de inicio de sesión cuando el usuario pulsa el botón.
     */
    fun iniciarSesion() {
        // Validación básica para no hacer llamadas innecesarias
        if (uiState.value.correo.isBlank() || uiState.value.clave.isBlank()) {
            _uiState.update { it.copy(mensajeError = "Correo y contraseña son obligatorios") }
            return
        }

        // Usamos viewModelScope para que la corrutina se cancele si el ViewModel se destruye
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) } // Activa el indicador de carga

            // 1. Busca al usuario en la base de datos a través del repositorio
            val usuario = usuarioRepository.obtenerUsuarioPorCorreo(uiState.value.correo.trim())

            // 2. Comprueba el resultado
            if (usuario == null) {
                // Si no hay usuario con ese correo
                _uiState.update { it.copy(mensajeError = "El usuario no existe", isLoading = false) }
            } else if (usuario.clave != uiState.value.clave) {
                // Si la contraseña no coincide (en una app real, usarías encriptación)
                _uiState.update { it.copy(mensajeError = "Contraseña incorrecta", isLoading = false) }
            } else {
                // 3. ¡Login correcto!
                // Guarda el ID del usuario en DataStore para mantener la sesión
                sessionRepository.saveUserId(usuario.id)
                // Actualiza el estado para que la UI pueda navegar a la siguiente pantalla
                _uiState.update { it.copy(loginExitoso = true, isLoading = false) }
            }
        }
    }

    /**
     * Se llama desde la UI para resetear el mensaje de error una vez que se ha mostrado.
     */
    fun errorMostrado() {
        _uiState.update { it.copy(mensajeError = null) }
    }
}
