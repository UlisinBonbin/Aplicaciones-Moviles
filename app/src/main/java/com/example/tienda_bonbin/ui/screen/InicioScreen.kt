package com.example.tienda_bonbin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tienda_bonbin.navigation.Screen
import com.example.tienda_bonbin.ui.theme.ChocolateBrown // <-- IMPORTACIÓN AÑADIDA
import com.example.tienda_bonbin.ui.theme.CreamBackground
import com.example.tienda_bonbin.ui.theme.DarkTextColor
import com.example.tienda_bonbin.ui.theme.SoftPink
import com.example.tienda_bonbin.viewmodels.UsuarioViewModel

@Composable
fun InicioScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val estado by viewModel.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground) // <-- FONDO COLOR CREMA
            .padding(32.dp), // Aumentamos el padding para un look más centrado
        verticalArrangement = Arrangement.Center, // Centramos el contenido verticalmente
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡Bienvenido de vuelta!",
            style = MaterialTheme.typography.headlineMedium,
            color = DarkTextColor,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Campo de Correo
        OutlinedTextField(
            value = estado.correo,
            onValueChange = viewModel::onCorreoChange,
            label = { Text("Correo Electrónico") },
            isError = estado.errores.correo != null,
            supportingText = {
                estado.errores.correo?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors( // <-- Colores personalizados
                focusedBorderColor = SoftPink,
                focusedLabelColor = SoftPink,
                cursorColor = SoftPink,
                unfocusedBorderColor = ChocolateBrown.copy(alpha = 0.7f)
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Campo de Contraseña
        OutlinedTextField(
            value = estado.clave,
            onValueChange = viewModel::onClaveChange,
            label = { Text("Contraseña") },
            isError = estado.errores.clave != null,
            supportingText = {
                estado.errores.clave?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors( // <-- Colores personalizados
                focusedBorderColor = SoftPink,
                focusedLabelColor = SoftPink,
                cursorColor = SoftPink,
                unfocusedBorderColor = ChocolateBrown.copy(alpha = 0.7f)
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Principal "Iniciar Sesión"
        Button(
            onClick = {
                // Lógica de inicio de sesión...
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = SoftPink // <-- BOTÓN PRINCIPAL ROSA
            )
        ) {
            Text("Iniciar Sesión", color = Color.White)
        }

        Spacer(Modifier.height(8.dp))

        // Botón Secundario "Volver a Inicio"
        OutlinedButton(
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = SolidColor(ChocolateBrown) // <-- BORDE CHOCOLATE
            )
        ) {
            Text("Volver a Inicio", color = ChocolateBrown) // <-- Texto color chocolate
        }
    }
}
