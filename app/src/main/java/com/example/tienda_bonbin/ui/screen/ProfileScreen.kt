package com.example.tienda_bonbin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
// --- 1. IMPORTACIONES AÑADIDAS ---
import androidx.compose.material.icons.automirrored.filled.KeyboardBackspace
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
// --- 2. IMPORTACIÓN AÑADIDA ---
import com.example.tienda_bonbin.navigation.Screen
import com.example.tienda_bonbin.ui.theme.ChocolateBrown
import com.example.tienda_bonbin.ui.theme.CreamBackground
import com.example.tienda_bonbin.ui.theme.DarkTextColor
import com.example.tienda_bonbin.ui.theme.SoftPink
import com.example.tienda_bonbin.viewmodels.AppViewModelProvider
import com.example.tienda_bonbin.viewmodels.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    // Si el estado indica que la sesión se cerró, navegamos a la pantalla de inicio
    LaunchedEffect(uiState.sesionCerrada) {
        if (uiState.sesionCerrada) {
            // --- 3. NAVEGACIÓN CORREGIDA ---
            navController.navigate(Screen.Home.route) {
                // Limpiamos la pila de navegación para que no pueda volver al perfil
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ChocolateBrown)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(CreamBackground)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp))
            } else if (uiState.usuario != null) {
                val usuario = uiState.usuario!!

                // Avatar de perfil
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(SoftPink),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        // Usamos firstOrNull para evitar crashes si el nombre está vacío
                        text = usuario.nombre.firstOrNull()?.uppercase() ?: "?",
                        fontSize = 60.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Información del usuario
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        InfoRow(icon = Icons.Default.Person, label = "Nombre", value = usuario.nombre)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        InfoRow(icon = Icons.Default.Email, label = "Correo", value = usuario.correo)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        InfoRow(icon = Icons.Default.Home, label = "Dirección", value = usuario.direccion)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botón de Cerrar Sesión
                Button(
                    onClick = { viewModel.cerrarSesion() },
                    colors = ButtonDefaults.buttonColors(containerColor = ChocolateBrown),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar Sesión", color = Color.White)
                }

                // --- 4. BOTÓN AÑADIDO ---
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        navController.navigate(Screen.Home.route) {
                            // Limpia la pila hasta Home para evitar volver al perfil con el botón "atrás"
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = SolidColor(ChocolateBrown)
                    )
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardBackspace,
                        contentDescription = "Volver",
                        tint = ChocolateBrown
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Volver a Inicio", color = ChocolateBrown)
                }

            } else {
                // Mensaje en caso de que no se cargue el usuario
                Text(
                    "No se pudo cargar la información del usuario.",
                    modifier = Modifier.padding(top = 32.dp)
                )
            }
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = label, tint = SoftPink)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, color = DarkTextColor, fontWeight = FontWeight.Medium)
        }
    }
}
