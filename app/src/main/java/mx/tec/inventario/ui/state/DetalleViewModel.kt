package mx.tec.inventario.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import mx.tec.inventario.data.ProductoRepository
import mx.tec.inventario.domain.Producto
import mx.tec.inventario.ui.navigation.Route
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Un producto y las dos acciones que se hacen sobre él.
 *
 * El id no se lo pasa nadie a mano: `SavedStateHandle` trae los argumentos de
 * navegación, y de paso sobrevive a que el sistema mate el proceso.
 */
class DetalleViewModel(
    private val repository: ProductoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productoId: Int =
        checkNotNull(savedStateHandle.get<Int>(Route.ARG_PRODUCTO_ID))

    val producto: StateFlow<Producto?> =
        repository.observarPorId(productoId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(ESPERA_MS),
            initialValue = null
        )

    /** Vender es restar uno. La pantalla no vuelve a pedir nada: el Flow avisa. */
    fun venderUno() {
        val actual = producto.value ?: return
        if (actual.agotado) return
        viewModelScope.launch {
            repository.actualizar(actual.copy(cantidad = actual.cantidad - 1))
        }
    }

    /** `alTerminar` se llama cuando la fila ya no está, no antes. */
    fun borrar(alTerminar: () -> Unit) {
        val actual = producto.value ?: return
        viewModelScope.launch {
            repository.borrar(actual)
            alTerminar()
        }
    }

    private companion object {
        const val ESPERA_MS = 5_000L
    }
}