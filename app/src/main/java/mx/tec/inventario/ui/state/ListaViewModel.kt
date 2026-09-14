package mx.tec.inventario.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import mx.tec.inventario.data.ProductoRepository
import mx.tec.inventario.domain.Producto

class ListaViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    var textoBusqueda by mutableStateOf("")
        private set

    fun onTextoBusquedaChange(nuevoTexto: String) {
        textoBusqueda = nuevoTexto
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val productos: StateFlow<List<Producto>> = snapshotFlow { textoBusqueda }
        .flatMapLatest { texto ->
            if (texto.isBlank()) {
                repository.observarTodos()
            } else {
                repository.buscarPorNombre(texto)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(ESPERA_MS),
            initialValue = emptyList()
        )

    private companion object {
        const val ESPERA_MS = 5_000L
    }
}