package com.example.tienda_bonbin.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardBackspace
// --- 1. IMPORTACIONES AÑADIDAS ---
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
// --- (El resto de tus importaciones)
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
import com.example.tienda_bonbin.data.CarritoItemInfo
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

    // --- 2. LÓGICA PARA MOSTRAR MENSAJES (SNACKBAR) ---
    val snackbarHostState = remember { SnackbarHostState() }
    // Usamos LaunchedEffect para observar cambios en el mensaje del ViewModel
    LaunchedEffect(uiState.mensajeUsuario) {
        uiState.mensajeUsuario?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            // Informamos al ViewModel que el mensaje ya se mostró
            carritoViewModel.mensajeMostrado()
        }
    }

    Scaffold(
        // Añadimos el SnackbarHost para que los mensajes puedan aparecer
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardBackspace,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                // --- 3. BOTÓN DE VACIAR CARRITO AÑADIDO ---
                actions = {
                    // Solo mostramos el botón si hay items en el carrito
                    if (uiState.items.isNotEmpty()) {
                        IconButton(onClick = { carritoViewModel.vaciarCarrito() }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Vaciar carrito",
                                tint = Color.White
                            )
                        }
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
                            Text(
                                text = "$${"%,.0f".format(uiState.total).replace(',', '.')}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = DarkTextColor
                            )
                        }
                        Button(
                            // --- 4. ACCIÓN DEL BOTÓN DE PAGO CONECTADA ---
                            onClick = { carritoViewModel.finalizarCompra() },
                            colors = ButtonDefaults.buttonColors(containerColor = SoftPink)
                        ) {
                            // Cambiamos el texto para que sea más claro
                            Text("Finalizar Compra", color = Color.White)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        // El resto del código de la UI no necesita cambios, ya que es reactivo al estado.
        if (uiState.items.isEmpty()) {
            Box(
                modifier = Modifier.padding(innerPadding).fillMaxSize().background(CreamBackground),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    Text("Tu carrito está vacío", style = MaterialTheme.typography.headlineSmall, color = DarkTextColor, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Parece que aún no has añadido productos. ¡Explora nuestro catálogo!", style = MaterialTheme.typography.bodyLarge, color = DarkTextColor.copy(alpha = 0.7f), textAlign = TextAlign.Center)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding).fillMaxSize().background(CreamBackground),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.items, key = { it.productoId }) { item ->
                    // 5. Usamos el nuevo nombre del Composable
                    CarritoItemRow(item = item)
                }
            }
        }
    }
}

// Renombramos el Composable para mayor claridad y evitar posibles conflictos
@Composable
private fun CarritoItemRow(item: CarritoItemInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = item.imagenUrl).crossfade(true).build()
                ),
                contentDescription = item.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp).clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DarkTextColor)
                Text(text = "$${"%,.0f".format(item.precio).replace(',', '.')}", style = MaterialTheme.typography.bodyLarge, color = SoftPink, fontWeight = FontWeight.Medium)
            }
            Box(
                modifier = Modifier.background(CreamBackground, shape = MaterialTheme.shapes.small).padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("Cant: ${item.cantidad}", fontWeight = FontWeight.Bold, color = DarkTextColor, fontSize = 14.sp)
            }
        }
    }
}
