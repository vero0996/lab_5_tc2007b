package mx.tec.inventario.ui.state

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import mx.tec.inventario.InventarioApplication

/**
 * Cómo se construye cada ViewModel de la app.
 *
 * Antes bastaba `viewModel()`: los ViewModels se construían solos porque sus
 * dependencias también. Con una base de datos de por medio ya no, así que hay
 * que decirle a Compose cómo hacerlo.
 */
object AppViewModelProvider {

    val Factory = viewModelFactory {

        initializer { ListaViewModel(inventarioApplication().container.productoRepository) }

        initializer {
            DetalleViewModel(
                repository = inventarioApplication().container.productoRepository,
                savedStateHandle = createSavedStateHandle()
            )
        }

        initializer {
            FormularioViewModel(
                repository = inventarioApplication().container.productoRepository,
                savedStateHandle = createSavedStateHandle()
            )
        }
    }
}

/** El atajo para llegar al contenedor desde dentro de un initializer. */
private fun CreationExtras.inventarioApplication(): InventarioApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as InventarioApplication