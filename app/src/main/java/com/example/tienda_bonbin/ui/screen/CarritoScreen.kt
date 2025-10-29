package com.example.tienda_bonbin.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardBackspace
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.tienda_bonbin.data.Producto
import com.example.tienda_bonbin.ui.theme.ChocolateBrown
import com.example.tienda_bonbin.ui.theme.CreamBackground
import com.example.tienda_bonbin.ui.theme.DarkTextColor
import com.example.tienda_bonbin.ui.theme.SoftPink

// --- 1. SE ELIMINAN LOS DATOS DE EJEMPLO ---
// Ya no necesitamos la lista de productos ni el total fijo.
// private val productosEnCarrito = listOf(...)
// private const val TOTAL_CARRITO = 35490.0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(navController: NavController) {

    // --- 2. SE CREA UNA LISTA VACÍA PARA LA LÓGICA ---
    // En el futuro, esta lista vendrá de un ViewModel. Por ahora, estará vacía.
    val productosEnCarrito: List<Producto> = emptyList()

    Scaffold(
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ChocolateBrown)
            )
        },
        bottomBar = {
            // La barra inferior solo se mostrará si el carrito NO está vacío.
            if (productosEnCarrito.isNotEmpty()) {
                BottomAppBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total:", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                            Text(
                                // --- 3. SE CALCULARÁ EL TOTAL DINÁMICAMENTE (FUTURO) ---
                                // Por ahora, mostramos "0" si se llegara a ver.
                                text = "$0",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = DarkTextColor
                            )
                        }
                        Button(
                            onClick = { /* TODO: Lógica para proceder al pago */ },
                            colors = ButtonDefaults.buttonColors(containerColor = SoftPink)
                        ) {
                            Text("Proceder al Pago", color = Color.White)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        // --- 4. LA LÓGICA DE LA UI AHORA FUNCIONA CON LA LISTA (VACÍA) ---
        if (productosEnCarrito.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(CreamBackground),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                ) {
                    Text(
                        "Tu carrito está vacío",
                        style = MaterialTheme.typography.headlineSmall,
                        color = DarkTextColor,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Parece que aún no has añadido productos. ¡Explora nuestro catálogo!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = DarkTextColor.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(CreamBackground),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productosEnCarrito) { producto ->
                    CarritoItem(producto = producto)
                }
            }
        }
    }
}

@Composable
private fun CarritoItem(producto: Producto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = producto.imagenUrl)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = producto.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextColor
                )
                Text(
                    text = "$${"%,.0f".format(producto.precio).replace(',', '.')}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = SoftPink,
                    fontWeight = FontWeight.Medium
                )
            }

            Box(
                modifier = Modifier
                    .background(CreamBackground, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("Cant: 1", fontWeight = FontWeight.Bold, color = DarkTextColor, fontSize = 14.sp)
            }
        }
    }
}