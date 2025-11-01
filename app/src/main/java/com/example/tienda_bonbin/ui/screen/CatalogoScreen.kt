package com.example.tienda_bonbin.ui.screen

import androidx.activity.result.launch
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tienda_bonbin.data.Producto
import com.example.tienda_bonbin.ui.theme.ChocolateBrown
import com.example.tienda_bonbin.ui.theme.CreamBackground
import com.example.tienda_bonbin.ui.theme.DarkTextColor
import com.example.tienda_bonbin.ui.theme.SoftPink
import com.example.tienda_bonbin.viewmodels.AppViewModelProvider
import com.example.tienda_bonbin.viewmodels.CatalogoViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    catalogoViewModel: CatalogoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // --- 5. OBTENER EL ESTADO DESDE EL VIEWMODEL ---
    val uiState by catalogoViewModel.uiState.collectAsState()

    // --- 2. AÑADE ESTE BLOQUE PARA GESTIONAR EL SNACKBAR ---
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.mensajeUsuario) {
        // Si el mensaje no es nulo...
        uiState.mensajeUsuario?.let { mensaje ->
            // Lanza una corrutina para mostrar el Snackbar.
            scope.launch {
                snackbarHostState.showSnackbar(mensaje)
                // Una vez mostrado, le decimos al ViewModel que "limpie" el mensaje
                // para que no vuelva to aparecer si la pantalla se recompone.
                catalogoViewModel.mensajeMostrado()
            }
        }
    }
    Scaffold(
        //Esto añade el SnackBar al Scaffold
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                // --- 6. USAR LA LISTA DEL VIEWMODEL ---
                items(uiState.productos) { producto ->
                    ProductCardWithCartButton(
                        // Pasamos el objeto completo
                        producto = producto,
                        // Le decimos qué hacer cuando se pulse el botón
                        onAddToCartClicked = {
                            catalogoViewModel.agregarProductoAlCarrito(producto)
                        }
                    )
                }
            }
        }
    }
}

// --- 7. ACTUALIZAR LA FIRMA DE `ProductCardWithCartButton` ---
@Composable
fun ProductCardWithCartButton(
    producto: Producto,
    onAddToCartClicked: () -> Unit
) {
    Card(
        modifier = Modifier.height(260.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(producto.imagenUrl) // Usar el campo del objeto
                        .crossfade(true)
                        .build(),
                    contentDescription = producto.nombre, // Usar el campo del objeto
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium, maxLines = 2, color = DarkTextColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    // Formatear el precio directamente desde el Double
                    Text(
                        text = "$${"%,.0f".format(producto.precio).replace(',', '.')}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = SoftPink
                    )
                }
            }

            // Botón del carrito en esquina inferior derecha
            IconButton(
                // --- 8. CONECTAR LA ACCIÓN ONCLICK ---
                onClick = onAddToCartClicked,
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
