package com.example.tienda_bonbin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tienda_bonbin.navigation.Screen
import com.example.tienda_bonbin.data.Producto
import com.example.tienda_bonbin.ui.theme.ChocolateBrown
import com.example.tienda_bonbin.ui.theme.CreamBackground
import com.example.tienda_bonbin.ui.theme.DarkTextColor
import com.example.tienda_bonbin.ui.theme.SoftPink
import com.example.tienda_bonbin.viewmodels.MainViewModel

// El comentario de la paleta de colores se ha eliminado porque ahora está en ui/theme/Color.kt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Pastelería Mil Sabores",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White // Color de texto blanco para que contraste
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Lógica de búsqueda */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color.White)
                    }
                    IconButton(onClick = { navController.navigate(Screen.Carrito.route)}) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito de Compras", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ChocolateBrown, // <-- CAMBIO DE COLOR
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(CreamBackground) // <-- CAMBIO DE COLOR DE FONDO
        ) {
            HorizontalNavBar(viewModel = viewModel)
            WelcomeBanner()
            FeaturedProductsSection(navController = navController)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HorizontalNavBar(viewModel: MainViewModel) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        item { NavBarItem(text = "Inicio", onClick = { /* Ya estás aquí */ }, isSelected = true) }
        item { NavBarItem(text = "Catálogo", onClick = { viewModel.navigateTo(Screen.Catalogo) }) }
        item { NavBarItem(text = "Mi Perfil", onClick = { viewModel.navigateTo(Screen.Profile) }) }

        // --- ¡LÍNEA CORREGIDA! ---
        // Ahora navega a la ruta correcta "login_screen" que está definida en tu MainActivity
        item { NavBarItem(text = "Iniciar Sesión", onClick = { viewModel.navigateTo(Screen.Login) }) }

        item { NavBarItem(text = "Registro", onClick = { viewModel.navigateTo(Screen.Registro) }) }
    }
}

@Composable
fun NavBarItem(text: String, onClick: () -> Unit, isSelected: Boolean = false) {
    TextButton(onClick = onClick) {
        Text(
            text = text,
            color = if (isSelected) SoftPink else DarkTextColor, // <-- CAMBIO DE COLOR
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun WelcomeBanner() {
    val imageUrl = "https://tortasdelacasa.com/wp-content/uploads/2024/02/DSC4340-scaled.jpg"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Banner de pasteles",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "El Sabor que Crea Momentos",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tortas frescas, hechas con amor.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun FeaturedProductsSection(navController: NavController) {
    // --- ↓↓↓ AJUSTA ESTA LISTA ↓↓↓ ---
    val productos = listOf(
        Producto(
            id = 1, // ID de ejemplo
            imagenUrl = "https://images.aws.nestle.recipes/original/2024_10_23T06_40_18_badun_images.badun.es_tarta_fria_de_chocolate_blanco_con_frutas.jpg",
            nombre = "Torta de Chocolate Blanco",
            precio = 18500.0 // Ahora es un número (Double)
        ),
        Producto(
            id = 2,
            imagenUrl = "https://tortamaniaecuador.com/wp-content/uploads/2022/12/Vainilla-con-crema-pequena-300x300.png",
            nombre = "Pastel de Vainilla y Crema",
            precio = 12000.0
        ),
        Producto(
            id = 3,
            imagenUrl = "https://rhenania.cl/wp-content/uploads/2020/12/CIRUELA-MANJAR-BLANCO.jpg",
            nombre = "Torta de Ciruela y Manjar",
            precio = 16990.0
        )
    )

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Nuestras Creaciones",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkTextColor // <-- CAMBIO DE COLOR
            )
            TextButton(onClick = { /* Navegar a la pantalla de catálogo */ }) {
                Text("Ver todo", color = SoftPink) // <-- CAMBIO DE COLOR
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp), tint = SoftPink)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(productos) { producto ->
                ProductCard(
                    imageUrl = producto.imagenUrl, // Usar el campo correcto: `imagenUrl`
                    title = producto.nombre,       // Usar el campo correcto: `nombre`
                    // Formatear el precio de Double a String
                    price = "$${"%,.0f".format(producto.precio).replace(',', '.')}"
                )
            }
        }
    }
}

@Composable
fun ProductCard(imageUrl: String, title: String, price: String) {
    Card(
        modifier = Modifier.width(180.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { /* TODO: Navegar al detalle del producto */ },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Fondo blanco/claro para la tarjeta
    ) {
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
                    .height(120.dp)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, maxLines = 2, minLines = 2, color = DarkTextColor)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = price, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = SoftPink) // <-- CAMBIO DE COLOR
            }
        }
    }
}
