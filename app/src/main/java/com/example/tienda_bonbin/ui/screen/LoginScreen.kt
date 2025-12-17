package com.example.tienda_bonbin.ui.screen

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tienda_bonbin.navigation.Screen
import com.example.tienda_bonbin.ui.theme.ChocolateBrown
import com.example.tienda_bonbin.ui.theme.CreamBackground
import com.example.tienda_bonbin.ui.theme.DarkTextColor
import com.example.tienda_bonbin.ui.theme.SoftPink
import com.example.tienda_bonbin.viewmodels.AppViewModelProvider
import com.example.tienda_bonbin.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.loginExitoso) {
        if (uiState.loginExitoso) {
            Toast.makeText(context, "¡Sesión iniciada!", Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.Profile.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    LaunchedEffect(uiState.mensajeError) {
        uiState.mensajeError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.errorMostrado()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(color = SoftPink)
        } else {
            Text(
                text = "¡Bienvenido de vuelta!",
                style = MaterialTheme.typography.headlineMedium,
                color = DarkTextColor,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = uiState.correo,
                onValueChange = { viewModel.onLoginValueChange(correo = it) },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SoftPink,
                    focusedLabelColor = SoftPink,
                    cursorColor = SoftPink,
                    unfocusedBorderColor = ChocolateBrown.copy(alpha = 0.7f)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.clave,
                onValueChange = { viewModel.onLoginValueChange(clave = it) },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SoftPink,
                    focusedLabelColor = SoftPink,
                    cursorColor = SoftPink,
                    unfocusedBorderColor = ChocolateBrown.copy(alpha = 0.7f)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.iniciarSesion() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = SoftPink)
            ) {
                Text("Iniciar Sesión", color = Color.White)
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate(Screen.Registro.route)
                },
                modifier = Modifier.fillMaxWidth(),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(ChocolateBrown))
            ) {
                Text("¿No tienes cuenta? Regístrate", color = ChocolateBrown)
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    // Navega a la pantalla Home y limpia la pila para que sea el nuevo inicio
                    navController.navigate(Screen.Home.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(ChocolateBrown.copy(alpha = 0.6f)))
            ) {
                Text("Volver a Inicio", color = ChocolateBrown)
            }
        }
    }
}
