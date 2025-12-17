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

    fun onLoginValueChange(correo: String? = null, clave: String? = null) {
        _uiState.update { currentState ->
            currentState.copy(
                correo = correo ?: currentState.correo,
                clave = clave ?: currentState.clave
            )
        }
    }

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
                    val loginResponse = response.body()

                    if (loginResponse != null) {
                        val usuarioDeRed = loginResponse.usuario

                        sessionRepository.saveSession(
                            userId = usuarioDeRed.id!!.toInt(),
                            token = loginResponse.token,
                            role = usuarioDeRed.rol
                        )

                        val usuarioParaDb = UsuarioDb(
                            id = usuarioDeRed.id.toInt(),
                            nombre = usuarioDeRed.nombre,
                            apellido = usuarioDeRed.apellido,
                            correo = usuarioDeRed.correo,
                            clave = "",
                            direccion = usuarioDeRed.direccion,
                            rol = usuarioDeRed.rol
                        )
                        usuarioRepository.insertarUsuario(usuarioParaDb)

                        // Actualiza la UI para navegar a la siguiente pantalla
                        _uiState.update { it.copy(loginExitoso = true, isLoading = false) }

                    } else {
                        // Error: la respuesta fue 200 OK pero el cuerpo estaba vacío
                        _uiState.update { it.copy(mensajeError = "Respuesta inesperada del servidor", isLoading = false) }
                    }

                } else {
                    // Error del servidor (ej. 401 Unauthorized - contraseña incorrecta)
                    _uiState.update { it.copy(mensajeError = "Correo o contraseña incorrectos", isLoading = false) }
                }

            } catch (e: Exception) {
                // ERROR DE RED
                _uiState.update { it.copy(mensajeError = "No se pudo conectar al servidor: ${e.message}", isLoading = false) }
                e.printStackTrace()
            }
        }
    }

    fun errorMostrado() {
        _uiState.update { it.copy(mensajeError = null) }
    }

}
