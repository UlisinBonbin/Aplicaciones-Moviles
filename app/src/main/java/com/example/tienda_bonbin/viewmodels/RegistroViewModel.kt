package com.example.tienda_bonbin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tienda_bonbin.data.Usuario
import com.example.tienda_bonbin.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.util.Patterns

// Estado de la UI para el formulario de registro
data class RegistroUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val direccion: String = "",
    val terminosAceptados: Boolean = false,
    val registroExitoso: Boolean = false,
    val mensajeError: String? = null
)

class RegistroViewModel(private val repository: UsuarioRepository) : ViewModel() {

    // _uiState es privado y mutable, solo el ViewModel puede cambiarlo.
    private val _uiState = MutableStateFlow(RegistroUiState())
    // uiState es público e inmutable, la UI solo puede leerlo.
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    // Función para actualizar el estado desde la UI
    fun onRegistroValueChange(nombre: String? = null, correo: String? = null, clave: String? = null, confirmarClave: String? = null, direccion: String? = null, terminos: Boolean? = null) {
        _uiState.update { currentState ->
            currentState.copy(
                nombre = nombre ?: currentState.nombre,
                correo = correo ?: currentState.correo,
                clave = clave ?: currentState.clave,
                confirmarClave = confirmarClave ?: currentState.confirmarClave,
                direccion = direccion ?: currentState.direccion,
                terminosAceptados = terminos ?: currentState.terminosAceptados
            )
        }
    }

    // Función principal que se llamará al pulsar el botón de registrar
    fun registrarUsuario() {
        val state = uiState.value // Usamos una variable local para facilitar la lectura

        // Validación de datos
        if (state.nombre.isBlank() || state.correo.isBlank() || state.clave.isBlank() || state.direccion.isBlank()) {
            _uiState.update { it.copy(mensajeError = "Todos los campos son obligatorios") }
            return
        }

        // ---LÓGICA DE VALIDACIÓN DE CORREO ---
        if (!Patterns.EMAIL_ADDRESS.matcher(state.correo).matches()) {
            _uiState.update { it.copy(mensajeError = "El formato del correo electrónico no es válido") }
            return
        }

        if (state.clave != state.confirmarClave) {
            _uiState.update { it.copy(mensajeError = "Las contraseñas no coinciden") }
            return
        }

        if (state.clave.length < 6) { // Es una buena práctica validar también el largo de la contraseña
            _uiState.update { it.copy(mensajeError = "La contraseña debe tener al menos 6 caracteres") }
            return
        }

        if (!state.terminosAceptados) {
            _uiState.update { it.copy(mensajeError = "Debes aceptar los términos y condiciones") }
            return
        }

        // Si todo es válido, lanzamos una corrutina para interactuar con el repositorio
        viewModelScope.launch {
            try {
                val nuevoUsuario = Usuario(
                    nombre = uiState.value.nombre.trim(),
                    correo = uiState.value.correo.trim(),
                    clave = uiState.value.clave, // Más adelante la encriptaremos
                    direccion = uiState.value.direccion.trim()
                )
                repository.insertarUsuario(nuevoUsuario)
                _uiState.update { it.copy(registroExitoso = true) } // Informamos a la UI que todo fue bien
            } catch (e: Exception) {
                _uiState.update { it.copy(mensajeError = "Error al registrar el usuario: ${e.message}") }
            }
        }
    }

    // Función para limpiar el mensaje de error una vez mostrado
    fun errorMostrado() {
        _uiState.update { it.copy(mensajeError = null) }
    }
}
