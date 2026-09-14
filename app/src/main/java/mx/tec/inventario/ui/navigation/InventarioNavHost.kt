package mx.tec.inventario.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import mx.tec.inventario.ui.components.CargandoView
import mx.tec.inventario.ui.screens.DetalleScreen
import mx.tec.inventario.ui.screens.FormularioScreen
import mx.tec.inventario.ui.screens.ListaScreen
import mx.tec.inventario.ui.state.DetalleViewModel
import mx.tec.inventario.ui.state.FormularioViewModel
import mx.tec.inventario.ui.state.ListaViewModel
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import mx.tec.inventario.ui.state.AppViewModelProvider

@Composable
fun InventarioApp() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Route.LISTA) {

        composable(Route.LISTA) {
            val viewModel: ListaViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val productos by viewModel.productos.collectAsStateWithLifecycle()

            ListaScreen(
                productos = productos,
                textoBusqueda = viewModel.textoBusqueda,
                onTextoBusquedaChange = viewModel::onTextoBusquedaChange,
                onProductoClick = { id -> nav.navigate(Route.detalle(id)) },
                onNuevoClick = { nav.navigate(Route.NUEVO) }
            )
        }

        composable(
            route = Route.DETALLE,
            arguments = listOf(navArgument(Route.ARG_PRODUCTO_ID) { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt(Route.ARG_PRODUCTO_ID) ?: return@composable
            val viewModel: DetalleViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val producto by viewModel.producto.collectAsStateWithLifecycle()

// null mientras la consulta va en camino, y también el instante
// posterior a borrar, cuando la fila ya no existe.
            val actual = producto
            if (actual == null) {
                CargandoView()
            } else {
                DetalleScreen(
                    producto = actual,
                    onVenderUno = { viewModel.venderUno() },
                    onEditar = { nav.navigate(Route.editar(id)) },
                    onBorrar = { viewModel.borrar { nav.popBackStack(Route.LISTA, false) } },
                    onBack = { nav.popBackStack() }
                )
            }
        }

        composable(Route.NUEVO) {
            val viewModel: FormularioViewModel = viewModel(factory = AppViewModelProvider.Factory)

            FormularioScreen(
                uiState = viewModel.uiState,
                esEdicion = viewModel.esEdicion,
                onNombreChange = viewModel::onNombreChange,
                onPrecioChange = viewModel::onPrecioChange,
                onCantidadChange = viewModel::onCantidadChange,
                onGuardar = { viewModel.guardar { nav.popBackStack() } },
                onCancelar = { nav.popBackStack() }
            )
        }

        composable(
            route = Route.EDITAR,
            arguments = listOf(navArgument(Route.ARG_PRODUCTO_ID) { type = NavType.IntType })
        ) {
            val viewModel: FormularioViewModel = viewModel(factory = AppViewModelProvider.Factory)

            FormularioScreen(
                uiState = viewModel.uiState,
                esEdicion = viewModel.esEdicion,
                onNombreChange = viewModel::onNombreChange,
                onPrecioChange = viewModel::onPrecioChange,
                onCantidadChange = viewModel::onCantidadChange,
                onGuardar = { viewModel.guardar { nav.popBackStack() } },
                onCancelar = { nav.popBackStack() }
            )
        }
    }
}
