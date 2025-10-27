package com.example.tienda_bonbin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tienda_bonbin.navigation.Screen
import com.example.tienda_bonbin.ui.theme.ChocolateBrown
import com.example.tienda_bonbin.ui.theme.CreamBackground
import com.example.tienda_bonbin.ui.theme.DarkTextColor
import com.example.tienda_bonbin.ui.theme.SoftPink
import com.example.tienda_bonbin.viewmodels.UsuarioViewModel

@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
){
    val estado by viewModel.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground) // <-- 1. FONDO COLOR CREMA
            .padding(32.dp)
            .verticalScroll(rememberScrollState()), // Permite scroll si el contenido es mucho
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Crea tu cuenta",
            style = MaterialTheme.typography.headlineMedium,
            color = DarkTextColor,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Estilo unificado para los OutlinedTextField
        val textFieldColors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SoftPink,
            focusedLabelColor = SoftPink,
            cursorColor = SoftPink,
            unfocusedBorderColor = ChocolateBrown.copy(alpha = 0.7f)
        )

        // --- CAMPOS DEL FORMULARIO ---

        OutlinedTextField(
            value = estado.nombre,
            onValueChange = viewModel::onNombreChange,
            label = { Text("Nombre") },
            isError = estado.errores.nombre!=null,
            supportingText = {
                estado.errores.nombre?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = estado.correo,
            onValueChange = viewModel::onCorreoChange,
            label = {Text("Correo Electrónico")},
            isError = estado.errores.correo!=null,
            supportingText = {
                estado.errores.correo?.let{ Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = estado.clave,
            onValueChange = viewModel::onClaveChange,
            label = {Text("Contraseña")},
            isError = estado.errores.clave!=null,
            supportingText = {
                estado.errores.clave?.let{ Text(it, color = MaterialTheme.colorScheme.error) }
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = estado.direccion,
            onValueChange = viewModel::onDireccionChange,
            label = {Text("Dirección")},
            isError = estado.errores.direccion!=null,
            supportingText = {
                estado.errores.direccion?.let{ Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = estado.aceptaTerminos,
                onCheckedChange = viewModel::onAceptarTerminosChange,
                colors = CheckboxDefaults.colors( // <-- Colores personalizados para el Checkbox
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
            onClick = {
                if (viewModel.validarFormulario()){
                    // navController.navigate("resumen") // Asegúrate de que esta ruta existe
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = SoftPink // <-- 2. BOTÓN PRINCIPAL ROSA
            )
        ) { Text("Registrar", color = androidx.compose.ui.graphics.Color.White)}

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = SolidColor(ChocolateBrown) // <-- 3. BORDE CHOCOLATE
            )
        ) {
            Text("Volver a Inicio", color = ChocolateBrown)
        }
    }
}
