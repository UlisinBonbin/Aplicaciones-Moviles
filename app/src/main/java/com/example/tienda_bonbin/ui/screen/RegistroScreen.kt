package com.example.tienda_bonbin.ui.screen // Tu paquete podría ser 'screens' o 'screen'

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tienda_bonbin.navigation.Screen // Asumo que tienes esta clase para tus rutas
import com.example.tienda_bonbin.ui.theme.*
import com.example.tienda_bonbin.viewmodels.AppViewModelProvider
import com.example.tienda_bonbin.viewmodels.RegistroViewModel // <-- CAMBIO 1: Importamos el ViewModel correcto

@Composable
fun RegistroScreen(
    navController: NavController,
    // <-- CAMBIO 2: El ViewModel se inyecta con la Factory. No se pasa como parámetro desde la navegación.
    viewModel: RegistroViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    // <-- CAMBIO 3: Nos conectamos al 'uiState' del nuevo ViewModel.
    val uiState by viewModel.uiState.collectAsState()

    // --- MANEJO DE EFECTOS (Navegación y Errores) ---
    // Este código es nuevo, pero fundamental para la navegación y mostrar errores.

    // 1. Navega a la pantalla de Login/Home cuando el registro es exitoso.
    LaunchedEffect(uiState.registroExitoso) {
        if (uiState.registroExitoso) {
            // Navega a la pantalla de login (o la que tú decidas)
            navController.navigate(Screen.Home.route) { // Cambia "Screen.Home.route" por tu ruta de login si es diferente
                // Limpia la pila para que el usuario no pueda volver al registro
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    // 2. Muestra un Snackbar cuando hay un error.
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.mensajeError) {
        uiState.mensajeError?.let { error ->
            snackbarHostState.showSnackbar(message = error)
            viewModel.errorMostrado() // Avisa al ViewModel que el error ya fue mostrado
        }
    }

    // Usamos Scaffold para poder tener un Snackbar
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = CreamBackground // Fondo de color crema para toda la pantalla
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues) // <-- Padding del Scaffold
                .fillMaxSize()
                .padding(horizontal = 32.dp) // Padding horizontal que ya tenías
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Crea tu cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = DarkTextColor,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SoftPink,
                focusedLabelColor = SoftPink,
                cursorColor = SoftPink,
                unfocusedBorderColor = ChocolateBrown.copy(alpha = 0.7f),
                errorBorderColor = MaterialTheme.colorScheme.error // Color para el borde cuando hay error
            )

            // --- CAMPOS DEL FORMULARIO (CONECTADOS AL NUEVO VIEWMODEL) ---

            OutlinedTextField(
                // <-- CAMBIO 4: Conectar cada campo al 'uiState' y a 'onRegistroValueChange'
                value = uiState.nombre,
                onValueChange = { viewModel.onRegistroValueChange(nombre = it) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.correo,
                onValueChange = { viewModel.onRegistroValueChange(correo = it) },
                label = {Text("Correo Electrónico")},
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.clave,
                onValueChange = { viewModel.onRegistroValueChange(clave = it) },
                label = {Text("Contraseña")},
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            // He añadido el campo de confirmar clave que definimos en el ViewModel
            OutlinedTextField(
                value = uiState.confirmarClave,
                onValueChange = { viewModel.onRegistroValueChange(confirmarClave = it) },
                label = {Text("Confirmar Contraseña")},
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.direccion,
                onValueChange = { viewModel.onRegistroValueChange(direccion = it) },
                label = {Text("Dirección")},
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.terminosAceptados,
                    onCheckedChange = { viewModel.onRegistroValueChange(terminos = it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = SoftPink,
                        uncheckedColor = ChocolateBrown
                    )
                )
                Spacer(Modifier.width(8.dp))
                Text("Acepto los términos y condiciones", color = DarkTextColor)
            }
            Spacer(modifier = Modifier.height(24.dp))

            // --- BOTONES DE ACCIÓN ---

            Button(
                // <-- CAMBIO 5: La lógica de validación ahora está en el ViewModel
                onClick = { viewModel.registrarUsuario() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = SoftPink)
            ) { Text("Registrar", color = Color.White)}

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = SolidColor(ChocolateBrown)
                )
            ) { Text("Volver a Inicio", color = ChocolateBrown) }
        }
    }
}
