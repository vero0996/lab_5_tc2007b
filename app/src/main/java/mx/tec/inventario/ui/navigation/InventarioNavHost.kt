package mx.tec.inventario.ui.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import mx.tec.inventario.ui.components.CargandoView
import mx.tec.inventario.ui.screens.DetalleScreen
import mx.tec.inventario.ui.screens.FormularioScreen
import mx.tec.inventario.ui.screens.ListaScreen
import mx.tec.inventario.ui.state.AppViewModelProvider
import mx.tec.inventario.ui.state.DetalleViewModel
import mx.tec.inventario.ui.state.FormularioViewModel
import mx.tec.inventario.ui.state.ListaViewModel

@Composable
fun InventarioApp() {
    val nav = rememberNavController()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    NavHost(navController = nav, startDestination = Route.LISTA) {

        composable(Route.LISTA) {
            val viewModel: ListaViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val productos by viewModel.productos.collectAsStateWithLifecycle()

            ListaScreen(
                productos = productos,
                textoBusqueda = viewModel.textoBusqueda,
                onTextoBusquedaChange = viewModel::onTextoBusquedaChange,
                onProductoClick = { id -> nav.navigate(Route.detalle(id)) },
                onNuevoClick = { nav.navigate(Route.NUEVO) },
                snackbarHostState = snackbarHostState
            )
        }

        composable(
            route = Route.DETALLE,
            arguments = listOf(navArgument(Route.ARG_PRODUCTO_ID) { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt(Route.ARG_PRODUCTO_ID) ?: return@composable
            val viewModel: DetalleViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val producto by viewModel.producto.collectAsStateWithLifecycle()

            val actual = producto
            if (actual == null) {
                CargandoView()
            } else {
                DetalleScreen(
                    producto = actual,
                    onVenderUno = { viewModel.venderUno() },
                    onEditar = { nav.navigate(Route.editar(id)) },
                    onBorrar = {
                        val eliminado = actual
                        viewModel.borrar {
                            nav.popBackStack(Route.LISTA, false)
                            // 3. Muestra el Snackbar con la opción de Deshacer
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "${eliminado.nombre} eliminado",
                                    actionLabel = "Deshacer",
                                    duration = SnackbarDuration.Short
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.reinsertar(eliminado)
                                }
                            }
                        }
                    },
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