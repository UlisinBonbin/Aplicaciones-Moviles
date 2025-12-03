package com.example.tienda_bonbin.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// 1. EL VIEWMODEL YA NO NECESITA CONOCER APISERVICE NI NETWORKMODULE
import com.example.tienda_bonbin.data.model.Usuario as NetworkUsuario
import com.example.tienda_bonbin.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistroUiState(
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val direccion: String = "",
    val terminosAceptados: Boolean = false,
    val isLoading: Boolean = false,
    val registroExitoso: Boolean = false,
    val mensajeError: String? = null
)

// 2. EL CONSTRUCTOR AHORA SOLO DEPENDE DEL REPOSITORIO
class RegistroViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    fun onRegistroValueChange(nombre: String? = null, apellido: String? = null, correo: String? = null, clave: String? = null, confirmarClave: String? = null, direccion: String? = null, terminos: Boolean? = null) {
        _uiState.update { currentState ->
            currentState.copy(
                nombre = nombre ?: currentState.nombre,
                apellido = apellido ?: currentState.apellido,
                correo = correo ?: currentState.correo,
                clave = clave ?: currentState.clave,
                confirmarClave = confirmarClave ?: currentState.confirmarClave,
                direccion = direccion ?: currentState.direccion,
                terminosAceptados = terminos ?: currentState.terminosAceptados,
                mensajeError = null
            )
        }
    }

    // 3. FUNCIÓN DE REGISTRO SIMPLIFICADA
    fun registrarUsuario() {
        val state = uiState.value

        // Se mantienen las validaciones de la UI
        if (state.nombre.isBlank() || state.apellido.isBlank() || state.correo.isBlank() || state.clave.isBlank() || state.direccion.isBlank()) {
            _uiState.update { it.copy(mensajeError = "Todos los campos son obligatorios") }
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(state.correo).matches()) {
            _uiState.update { it.copy(mensajeError = "El formato del correo electrónico no es válido") }
            return
        }
        if (state.clave != state.confirmarClave) {
            _uiState.update { it.copy(mensajeError = "Las contraseñas no coinciden") }
            return
        }
        if (state.clave.length < 6) {
            _uiState.update { it.copy(mensajeError = "La contraseña debe tener al menos 6 caracteres") }
            return
        }
        if (!state.terminosAceptados) {
            _uiState.update { it.copy(mensajeError = "Debes aceptar los términos y condiciones") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Se crea el objeto de red que espera el repositorio
            val nuevoUsuario = NetworkUsuario(
                nombre = state.nombre.trim(),
                apellido = state.apellido.trim(),
                correo = state.correo.trim(),
                contrasena = state.clave,
                direccion = state.direccion.trim(),
                rol = "CLIENTE"
            )

            // 4. SE DELEGA TODA LA LÓGICA AL REPOSITORIO
            val resultado = usuarioRepository.registrarUsuario(nuevoUsuario)

            // 5. SE ACTUALIZA LA UI BASÁNDOSE EN EL RESULTADO
            resultado.onSuccess {
                _uiState.update { it.copy(isLoading = false, registroExitoso = true) }
            }
            resultado.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, mensajeError = error.message ?: "Ocurrió un error desconocido") }
            }
        }
    }

    fun errorMostrado() {
        _uiState.update { it.copy(mensajeError = null) }
    }
}
