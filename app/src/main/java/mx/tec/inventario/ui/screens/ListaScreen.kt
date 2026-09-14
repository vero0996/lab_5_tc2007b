package mx.tec.inventario.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.inventario.domain.Producto
import mx.tec.inventario.ui.components.ProductoCard
import mx.tec.inventario.ui.theme.InventarioTheme
import androidx.compose.runtime.remember

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(
    productos: List<Producto>,
    textoBusqueda: String,
    onTextoBusquedaChange: (String) -> Unit,
    onProductoClick: (Int) -> Unit,
    onNuevoClick: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)},
        floatingActionButton = {
            FloatingActionButton(onClick = onNuevoClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar producto")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) { // 👈 Faltaba esta llave '{'
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = onTextoBusquedaChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Buscar por nombre...") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            LazyColumn {
                items(productos) { producto ->
                    ProductoCard(
                        producto = producto,
                        onClick = { onProductoClick(producto.id) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaPreview() {
    val demo = listOf(
        Producto(1, "Café de olla 1 kg", 189.0, 12),
        Producto(2, "Miel de agave 500 ml", 95.5, 4),
        Producto(3, "Chocolate de mesa", 64.0, 0)
    )
    InventarioTheme {
        ListaScreen(
            productos = demo,
            textoBusqueda = "",
            onTextoBusquedaChange = {},
            onProductoClick = {},
            onNuevoClick = {},
            snackbarHostState = remember { SnackbarHostState ()}
        )
    }
}

@Preview(showBackground = true, name = "Vacío")
@Composable
private fun ListaVaciaPreview() {
    InventarioTheme {
        ListaScreen(
            productos = emptyList(),
            textoBusqueda = "",
            onTextoBusquedaChange = {},
            onProductoClick = {},
            onNuevoClick = {},
            snackbarHostState = remember { SnackbarHostState ()}
        )
    }
}