package mx.tec.inventario.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import mx.tec.inventario.data.ProductoRepository
import mx.tec.inventario.domain.Producto

/**
 * La lista del inventario.
 *
 * Ya no hay ningún `recargar()` que llamar: el `Flow` del repositorio
 * empuja una lista nueva cada vez que la tabla cambia. El ViewModel solo lo
 * convierte en algo que Compose sabe leer.
 *
 * `null` NO es lo mismo que lista vacía: null es "la primera consulta todavía
 * no vuelve", vacía es "no hay productos". El usuario lee cosas distintas.
 */
class ListaViewModel(repository: ProductoRepository) : ViewModel() {

    val productos: StateFlow<List<Producto>?> =
        repository.observarTodos().stateIn(
            scope = viewModelScope,
            // Deja de escuchar 5 s después de que la pantalla se va, para no
            // reabrir la consulta en cada rotación.
            started = SharingStarted.WhileSubscribed(ESPERA_MS),
            initialValue = null
        )

    private companion object {
        const val ESPERA_MS = 5_000L
    }
}