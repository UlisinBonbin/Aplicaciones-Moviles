package com.example.tienda_bonbin.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tienda_bonbin.data.ApiService
// 1. Asegúrate de que el import de Usuario apunte a la clase del paquete 'model'
import com.example.tienda_bonbin.data.model.Usuario
import com.example.tienda_bonbin.data.NetworkModule
import com.example.tienda_bonbin.repository.UsuarioRepository
// Quita la dependencia del Repositorio por ahora, llamaremos a la red directamente
// import com.example.tienda_bonbin.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 2. Añadimos un campo 'isLoading' para mostrar una barra de progreso
data class RegistroUiState(
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val direccion: String = "",
    val terminosAceptados: Boolean = false,
    val isLoading: Boolean = false, // Para saber cuándo mostrar un spinner de carga
    val registroExitoso: Boolean = false,
    val mensajeError: String? = null
)

// 3. Quitamos el repositorio del constructor por ahora para simplificar
class RegistroViewModel(private val usuarioRepository: UsuarioRepository, private val apiService: ApiService) : ViewModel() {

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
                mensajeError = null // Limpiamos el error al cambiar un campo
            )
        }
    }

    // 4. --- FUNCIÓN PRINCIPAL ACTUALIZADA ---
    fun registrarUsuario() {
        val state = uiState.value

        // Mantenemos todas tus excelentes validaciones
        if (state.nombre.isBlank() || state.apellido.isBlank() ||state.correo.isBlank() || state.clave.isBlank() || state.direccion.isBlank()) {
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

        // Si todo es válido, lanzamos la corrutina para llamar a la API
        viewModelScope.launch {
            try {
                // Estado de carga: ON
                _uiState.update { it.copy(isLoading = true) }

                val nuevoUsuario = Usuario(
                    nombre = state.nombre.trim(),
                    apellido = state.apellido.trim(),
                    correo = state.correo.trim(),
                    contrasena = state.clave, // Usamos el nombre del campo del JSON: "contrasena"
                    direccion = state.direccion.trim(),
                    rol = "CLIENTE"
                )

                // --- ¡AQUÍ ESTÁ LA MAGIA! ---
                // Llamamos directamente al ApiService que creamos en NetworkModule
                val response = apiService.registrarUsuario(nuevoUsuario)

                // --- 5. ---

                if (response.isSuccessful && response.body() != null) {
                    // ÉXITO: El servidor respondió con un código 2xx
                    val usuarioRegistrado = response.body()!!
                    println("¡Usuario registrado con éxito en el servidor!: $usuarioRegistrado")

                    val usuarioParaDb = com.example.tienda_bonbin.data.Usuario(
                        id = usuarioRegistrado.id!!.toInt(),
                        nombre = usuarioRegistrado.nombre,
                        apellido = usuarioRegistrado.apellido,
                        correo = usuarioRegistrado.correo,
                        clave = "",
                        direccion = usuarioRegistrado.direccion,
                        rol = "CLIENTE"
                    )

                    usuarioRepository.insertarUsuario(usuarioParaDb)
                    _uiState.update { it.copy(isLoading = false, registroExitoso = true) }

                    // Opcional: Aquí podrías guardar el usuario en tu base de datos local (Room)
                    // si tuvieras un repositorio para ello.
                } else {
                    // ERROR DEL SERVIDOR: El servidor respondió con un error (4xx, 5xx)
                    val errorBody = response.errorBody()?.string()
                    println("Error del servidor: ${response.code()} - $errorBody")
                    _uiState.update { it.copy(isLoading = false, mensajeError = "Error del servidor: ${response.code()}. Revisa la consola del backend.") }
                }

            } catch (e: Exception) {
                // ERROR DE RED: No se pudo conectar al servidor (no hay internet, IP incorrecta, etc.)
                println("Error de red: ${e.message}")
                _uiState.update { it.copy(isLoading = false, mensajeError = "No se pudo conectar al servidor. Revisa tu conexión y la IP.") }
            }
        }
    }

    fun errorMostrado() {
        _uiState.update { it.copy(mensajeError = null) }
    }
}
