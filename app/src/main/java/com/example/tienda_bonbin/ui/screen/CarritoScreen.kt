package com.example.tienda_bonbin.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardBackspace
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
// ✅ 1. CAMBIO DE IMPORTACIÓN: Usamos el CarritoItem de la API, no el de Room.
import com.example.tienda_bonbin.data.model.CarritoItem
import com.example.tienda_bonbin.ui.theme.ChocolateBrown
import com.example.tienda_bonbin.ui.theme.CreamBackground
import com.example.tienda_bonbin.ui.theme.DarkTextColor
import com.example.tienda_bonbin.ui.theme.SoftPink
import com.example.tienda_bonbin.viewmodels.AppViewModelProvider
import com.example.tienda_bonbin.viewmodels.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    navController: NavController,
    carritoViewModel: CarritoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by carritoViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.mensajeUsuario) {
        uiState.mensajeUsuario?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            carritoViewModel.mensajeMostrado()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardBackspace, "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ChocolateBrown)
            )
        },
        bottomBar = {
            if (uiState.items.isNotEmpty()) {
                BottomAppBar(containerColor = Color.White, tonalElevation = 8.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total:", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                            // El formato del total no cambia
                            Text(
                                text = "$${"%,.2f".format(uiState.total)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = DarkTextColor
                            )
                        }
                        Button(
                            onClick = { carritoViewModel.finalizarCompra() },
                            colors = ButtonDefaults.buttonColors(containerColor = SoftPink)
                        ) {
                            Text("Finalizar Compra", color = Color.White)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (uiState.items.isEmpty()) {
            // La pantalla de carrito vacío no cambia
            Box(
                modifier = Modifier.padding(innerPadding).fillMaxSize().background(CreamBackground),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text("Tu carrito está vacío", style = MaterialTheme.typography.headlineSmall, color = DarkTextColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("¡Explora nuestro catálogo para añadir productos!", style = MaterialTheme.typography.bodyLarge, color = DarkTextColor.copy(alpha = 0.7f), textAlign = TextAlign.Center)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding).fillMaxSize().background(CreamBackground),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // ✅ 2. CAMBIO EN LA KEY: La key ahora es el 'id' del CarritoItem, que es único.
                items(uiState.items, key = { it.id }) { item ->
                    // ✅ 3. CAMBIO DE PARÁMETRO: Pasamos el 'item' completo y el lambda para borrarlo.
                    CarritoItemRow(
                        item = item,
                        onDeleteClicked = { carritoViewModel.eliminarItem(item.id) }
                    )
                }
            }
        }
    }
}

// ✅ 4. CAMBIO FUNDAMENTAL: El composable ahora recibe el 'CarritoItem' de la API.
@Composable
private fun CarritoItemRow(item: CarritoItem, onDeleteClicked: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            // ✅ 5. CAMBIO EN EL ACCESO A DATOS: Ahora accedemos a los datos a través de 'item.producto.propiedad'.
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = item.producto.imagenUrl) // <-- Acceso corregido
                        .crossfade(true).build()
                ),
                contentDescription = item.producto.nombre, // <-- Acceso corregido
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp).clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.producto.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DarkTextColor) // <-- Acceso corregido
                Text(text = "$${"%,.2f".format(item.producto.precio)}", style = MaterialTheme.typography.bodyLarge, color = SoftPink, fontWeight = FontWeight.Medium) // <-- Acceso corregido
            }
            Box(
                modifier = Modifier.background(CreamBackground, shape = MaterialTheme.shapes.small).padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("Cant: ${item.cantidad}", fontWeight = FontWeight.Bold, color = DarkTextColor, fontSize = 14.sp)
            }
            // ✅ 6. AÑADIMOS EL BOTÓN DE BORRAR
            IconButton(onClick = onDeleteClicked) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar item", tint = Color.Gray)
            }
        }
    }
}
