package com.example.tienda_bonbin.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tienda_bonbin.data.ApiService
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
class LoginViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val sessionRepository: SessionRepository,
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    // El onLoginValueChange no cambia.
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
        if (uiState.value.correo.isBlank() || uiState.value.clave.isBlank()) {
            _uiState.update { it.copy(mensajeError = "Correo y contraseña son obligatorios") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val loginRequest = LoginRequest(
                    correo = uiState.value.correo.trim(),
                    contrasena = uiState.value.clave
                )

                val response = apiService.login(loginRequest)

                // 3. Comprueba la respuesta del servidor
                if (response.isSuccessful) {
                    // ✅ 4. AHORA EL BODY ES UN 'LoginResponse', NO UN 'Usuario'
                    val loginResponse = response.body()

                    if (loginResponse != null) {
                        // ¡LOGIN ONLINE CORRECTO!
                        val usuarioDeRed = loginResponse.usuario

                        // ✅ 5. GUARDAMOS LA SESIÓN COMPLETA: ID, TOKEN Y ROL
                        sessionRepository.saveSession(
                            userId = usuarioDeRed.id!!.toInt(),
                            token = loginResponse.token,
                            role = usuarioDeRed.rol
                        )

                        // 6. GUARDAMOS EL USUARIO EN LA BASE DE DATOS LOCAL (Room)
                        //    (Esta parte es opcional pero la mantenemos si la usas en otras pantallas)
                        val usuarioParaDb = UsuarioDb(
                            id = usuarioDeRed.id.toInt(),
                            nombre = usuarioDeRed.nombre,
                            apellido = usuarioDeRed.apellido,
                            correo = usuarioDeRed.correo,
                            // No guardamos la contraseña en la base de datos local por seguridad.
                            clave = "",
                            direccion = usuarioDeRed.direccion,
                            rol = usuarioDeRed.rol // Guardamos también el rol
                        )
                        usuarioRepository.insertarUsuario(usuarioParaDb)

                        // 7. Actualiza la UI para navegar a la siguiente pantalla
                        _uiState.update { it.copy(loginExitoso = true, isLoading = false) }

                    } else {
                        // Error raro: la respuesta fue 200 OK pero el cuerpo estaba vacío
                        _uiState.update { it.copy(mensajeError = "Respuesta inesperada del servidor", isLoading = false) }
                    }

                } else {
                    // ERROR DEL SERVIDOR (ej. 401 Unauthorized - contraseña incorrecta)
                    _uiState.update { it.copy(mensajeError = "Correo o contraseña incorrectos", isLoading = false) }
                }

            } catch (e: Exception) {
                // ERROR DE RED
                _uiState.update { it.copy(mensajeError = "No se pudo conectar al servidor: ${e.message}", isLoading = false) }
                e.printStackTrace()
            }
        }
    }

    // errorMostrado() no cambia.
    fun errorMostrado() {
        _uiState.update { it.copy(mensajeError = null) }
    }

}
