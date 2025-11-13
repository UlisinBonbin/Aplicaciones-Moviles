package com.example.tienda_bonbin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tienda_bonbin.repository.SessionRepository
import com.example.tienda_bonbin.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.tienda_bonbin.data.NetworkModule
import com.example.tienda_bonbin.data.model.dto.LoginRequest

// Importa la entidad de la base de datos 'Usuario' y le pone un alias 'UsuarioDb'
// para no confundirla con el 'Usuario' del modelo de red.
import com.example.tienda_bonbin.data.Usuario as UsuarioDb

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
        // Validación básica, se queda igual
        if (uiState.value.correo.isBlank() || uiState.value.clave.isBlank()) {
            _uiState.update { it.copy(mensajeError = "Correo y contraseña son obligatorios") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) } // Activa el indicador de carga

            try {
                // 1. Crea el objeto para la petición de red
                val loginRequest = LoginRequest(
                    correo = uiState.value.correo.trim(),
                    contrasena = uiState.value.clave
                )

                // 2. Llama al servidor a través de Retrofit (NetworkModule)
                val response = NetworkModule.apiService.login(loginRequest)

                // 3. Comprueba la respuesta del servidor
                if (response.isSuccessful && response.body() != null) {
                    // ¡LOGIN ONLINE CORRECTO! El servidor respondió con 200 OK y un usuario
                    val usuarioDeRed = response.body()!!

                    // 4. Guarda el ID del usuario en DataStore para mantener la sesión
                    usuarioDeRed.id?.let { idNoNulo ->
                        sessionRepository.saveUserId(idNoNulo.toInt())
                    }

                    // 5. MAPEA y guarda el usuario en la BASE DE DATOS LOCAL (Room)
                    //    Así otras pantallas pueden leer los datos del usuario rápidamente.
                    val usuarioParaDb = UsuarioDb(
                        id = usuarioDeRed.id!!.toInt(),
                        nombre = usuarioDeRed.nombre,
                        apellido = usuarioDeRed.apellido,
                        correo = usuarioDeRed.correo,
                        clave = usuarioDeRed.contrasena,
                        direccion = usuarioDeRed.direccion
                    )
                    usuarioRepository.insertarUsuario(usuarioParaDb)

                    // 6. Actualiza la UI para que pueda navegar a la siguiente pantalla
                    _uiState.update { it.copy(loginExitoso = true, isLoading = false) }

                } else {
                    // ERROR DEL SERVIDOR (ej. 401 Unauthorized - contraseña incorrecta)
                    // El servidor respondió, pero con un código de error.
                    _uiState.update { it.copy(mensajeError = "Correo o contraseña incorrectos", isLoading = false) }
                }

            } catch (e: Exception) {
                // ERROR DE RED (No hay internet, IP incorrecta, el servidor está caído, etc.)
                // La llamada de red falló antes de poder recibir una respuesta.
                _uiState.update { it.copy(mensajeError = "No se pudo conectar al servidor", isLoading = false) }
                // Imprimimos el error en la consola para depuración
                e.printStackTrace()
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
