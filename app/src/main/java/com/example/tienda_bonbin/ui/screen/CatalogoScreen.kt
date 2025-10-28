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
import com.example.tienda_bonbin.ui.theme.ChocolateBrown
import com.example.tienda_bonbin.ui.theme.CreamBackground
import com.example.tienda_bonbin.ui.theme.DarkTextColor
import com.example.tienda_bonbin.ui.theme.SoftPink

// ✅ Reutiliza tu data class Product ya definido en HomeScreen.kt

private val catalogoDeProductos = listOf(
    Product("https://tortasdelacasa.com/wp-content/uploads/2024/02/DSC4340-scaled.jpg", "Torta de Frutos Rojos", "$22.000"),
    Product("https://images.aws.nestle.recipes/original/2024_10_23T06_40_18_badun_images.badun.es_tarta_fria_de_chocolate_blanco_con_frutas.jpg", "Torta Chocolate Blanco", "$18.500"),
    Product("https://tortamaniaecuador.com/wp-content/uploads/2022/12/Vainilla-con-crema-pequena-300x300.png", "Pastel de Vainilla", "$12.000"),
    Product("https://rhenania.cl/wp-content/uploads/2020/12/CIRUELA-MANJAR-BLANCO.jpg", "Torta de Ciruela", "$16.990"),
    Product("https://www.elinasaiach.com/wp-content/uploads/2022/04/Mousse-Chocolate-3.jpg", "Mousse de Chocolate", "$9.500"),
    Product("https://recetasdecocina.elmundo.es/wp-content/uploads/2022/08/tiramisu-postre-italiano.jpg", "Tiramisú Italiano", "$15.000"),
    Product("https://www.lomismoperosano.cl/wp-content/uploads/2022/01/receta-torta-panqueque-naranja.jpg", "Torta Panqueque Naranja", "$19.990"),
    Product("https://www.hola.com/horizon/landscape/64c21cd97107-tarta-horno-queso-t.jpg", "Tarta de Queso", "$14.500"),
    Product("https://cdn7.kiwilimon.com/recetaimagen/1366/960x640/2229.jpg.jpg", "Pastel Imposible", "$21.000"),
    Product("https://jetextramar.com/wp-content/uploads/2021/11/tarta-de-santiago1-empresa-de-alimentos.jpg", "Tarta de Santiago", "$13.000"),
    Product("https://azucarledesma.com/wp-content/uploads/2024/01/20240131-BrownieLight.jpg", "Brownie de Chocolate", "$8.000"),
    Product("https://dinkel.es/wp-content/uploads/2020/06/1041-Pan-sin-Gluten-2.png", "Pan sin Gluten", "$5.500"),
    Product("https://luciapaula.com/wp-content/uploads/2023/01/Blog-1970-01-20-125839033.jpg", "Cheesecake Frutos Rojos", "$17.800"),
    Product("https://i.blogs.es/8792e6/galletas-avea-tahina-datiles/840_560.jpg", "Galletas de Avena", "$6.000"),
    Product("https://i.pinimg.com/originals/aa/a0/4f/aaa04fb61005c9fc6d748dee6eac76f3.jpg", "Alfajores de Maicena", "$7.500"),
    Product("https://i.pinimg.com/474x/3b/bc/bb/3bbcbb826b865e5278f53a5b2661c2e5.jpg", "Donas Glaseadas", "$4.500")
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
                items(catalogoDeProductos) { producto ->
                    ProductCardWithCartButton(
                        imageUrl = producto.imageUrl,
                        title = producto.title,
                        price = producto.price
                    )
                }
            }
        }
    }
}

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

            // 🛒 Botón del carrito en esquina inferior derecha
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
