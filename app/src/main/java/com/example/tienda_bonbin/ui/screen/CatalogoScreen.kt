package com.example.tienda_bonbin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
// --- 1. AÑADIR LA IMPORTACIÓN CORRECTA ---
import com.example.tienda_bonbin.data.Producto
import com.example.tienda_bonbin.ui.theme.ChocolateBrown
import com.example.tienda_bonbin.ui.theme.CreamBackground
import com.example.tienda_bonbin.ui.theme.DarkTextColor
import com.example.tienda_bonbin.ui.theme.SoftPink

// --- 2. ACTUALIZAR LA LISTA DE PRODUCTOS ---
// Ahora usamos la data class `Producto` con id, nombre, precio (Double) e imagenUrl.
private val catalogoDeProductos = listOf(
    Producto(id = 1, nombre = "Torta de Frutos Rojos", precio = 22000.0, imagenUrl = "https://tortasdelacasa.com/wp-content/uploads/2024/02/DSC4340-scaled.jpg"),
    Producto(id = 2, nombre = "Torta Chocolate Blanco", precio = 18500.0, imagenUrl = "https://images.aws.nestle.recipes/original/2024_10_23T06_40_18_badun_images.badun.es_tarta_fria_de_chocolate_blanco_con_frutas.jpg"),
    Producto(id = 3, nombre = "Pastel de Vainilla", precio = 12000.0, imagenUrl = "https://tortamaniaecuador.com/wp-content/uploads/2022/12/Vainilla-con-crema-pequena-300x300.png"),
    Producto(id = 4, nombre = "Torta de Ciruela", precio = 16990.0, imagenUrl = "https://rhenania.cl/wp-content/uploads/2020/12/CIRUELA-MANJAR-BLANCO.jpg"),
    Producto(id = 5, nombre = "Mousse de Chocolate", precio = 9500.0, imagenUrl = "https://www.elinasaiach.com/wp-content/uploads/2022/04/Mousse-Chocolate-3.jpg"),
    Producto(id = 6, nombre = "Tiramisú Italiano", precio = 15000.0, imagenUrl = "https://recetasdecocina.elmundo.es/wp-content/uploads/2022/08/tiramisu-postre-italiano.jpg"),
    Producto(id = 7, nombre = "Torta Panqueque Naranja", precio = 19990.0, imagenUrl = "https://www.lomismoperosano.cl/wp-content/uploads/2022/01/receta-torta-panqueque-naranja.jpg"),
    Producto(id = 8, nombre = "Tarta de Queso", precio = 14500.0, imagenUrl = "https://www.hola.com/horizon/landscape/64c21cd97107-tarta-horno-queso-t.jpg"),
    Producto(id = 9, nombre = "Pastel Imposible", precio = 21000.0, imagenUrl = "https://cdn7.kiwilimon.com/recetaimagen/1366/960x640/2229.jpg.jpg"),
    Producto(id = 10, nombre = "Tarta de Santiago", precio = 13000.0, imagenUrl = "https://jetextramar.com/wp-content/uploads/2021/11/tarta-de-santiago1-empresa-de-alimentos.jpg"),
    Producto(id = 11, nombre = "Brownie de Chocolate", precio = 8000.0, imagenUrl = "https://azucarledesma.com/wp-content/uploads/2024/01/20240131-BrownieLight.jpg"),
    Producto(id = 12, nombre = "Pan sin Gluten", precio = 5500.0, imagenUrl = "https://dinkel.es/wp-content/uploads/2020/06/1041-Pan-sin-Gluten-2.png"),
    Producto(id = 13, nombre = "Cheesecake Frutos Rojos", precio = 17800.0, imagenUrl = "https://luciapaula.com/wp-content/uploads/2023/01/Blog-1970-01-20-125839033.jpg"),
    Producto(id = 14, nombre = "Galletas de Avena", precio = 6000.0, imagenUrl = "https://i.blogs.es/8792e6/galletas-avea-tahina-datiles/840_560.jpg"),
    Producto(id = 15, nombre = "Alfajores de Maicena", precio = 7500.0, imagenUrl = "https://i.pinimg.com/originals/aa/a0/4f/aaa04fb61005c9fc6d748dee6eac76f3.jpg"),
    Producto(id = 16, nombre = "Donas Glaseadas", precio = 4500.0, imagenUrl = "https://i.pinimg.com/474x/3b/bc/bb/3bbcbb826b865e5278f53a5b2661c2e5.jpg")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuestro Catálogo", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ChocolateBrown)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(CreamBackground)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 180.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // --- 3. ACTUALIZAR LA LLAMADA AL COMPOSABLE ---
                items(catalogoDeProductos) { producto ->
                    ProductCardWithCartButton(
                        imageUrl = producto.imagenUrl, // Usar el campo correcto
                        title = producto.nombre,       // Usar el campo correcto
                        // Formatear el precio de Double a String
                        price = "$${"%,.0f".format(producto.precio).replace(',', '.')}"
                    )
                }
            }
        }
    }
}

// El composable 'ProductCardWithCartButton' no necesita cambios, ya que sigue recibiendo Strings.
@Composable
fun ProductCardWithCartButton(imageUrl: String, title: String, price: String) {
    Card(
        modifier = Modifier.height(260.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = title, style = MaterialTheme.typography.titleMedium, maxLines = 2, color = DarkTextColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = price, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = SoftPink)
                }
            }

            // Botón del carrito en esquina inferior derecha
            IconButton(
                onClick = { /* TODO: lógica agregar al carrito */ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(36.dp)
                    .background(ChocolateBrown, shape = MaterialTheme.shapes.small)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Agregar al carrito",
                    tint = Color.White
                )
            }
        }
    }
}
