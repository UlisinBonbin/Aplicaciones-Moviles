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
import androidx.compose.runtime.*
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
import com.example.tienda_bonbin.data.model.Producto
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
    val uiState by catalogoViewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.mensajeUsuario) {
        uiState.mensajeUsuario?.let { mensaje ->
            scope.launch {
                snackbarHostState.showSnackbar(mensaje)
                catalogoViewModel.mensajeMostrado()
            }
        }
    }

    Scaffold(
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
                items(uiState.productos) { producto ->
                    ProductCardWithCartButton(
                        producto = producto,
                        onAddToCartClicked = {
                            catalogoViewModel.agregarProductoAlCarrito(producto)
                        }
                    )
                }
            }
        }
    }
}


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
                        .data(producto.imagenUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = producto.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium, maxLines = 2, color = DarkTextColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$${"%,.0f".format(producto.precio).replace(',', '.')}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = SoftPink
                    )
                }
            }

            IconButton(
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

