package mx.tec.inventario

import android.app.Application
import android.content.Context
import mx.tec.inventario.data.ProductoRepository
import mx.tec.inventario.data.local.InventarioDatabase

/**
 * El contenedor de dependencias: quién construye a quién, en un solo lugar.
 *
 * Existe porque el repositorio ya no se puede construir solo. Necesita un DAO,
 * que necesita la base, que necesita un `Context` — y un ViewModel no tiene
 * `Context`.
 *
 * `by lazy` significa que la base no se abre hasta que alguien pida el
 * repositorio por primera vez.
 */
class AppContainer(private val context: Context) {

    val productoRepository: ProductoRepository by lazy {
        ProductoRepository(InventarioDatabase.obtener(context).productoDao())
    }
}

/**
 * La clase Application vive tanto como el proceso. Es el lugar natural del
 * contenedor: se crea una vez, antes que cualquier pantalla.
 *
 * Hay que declararla en el manifiesto con `android:name=".InventarioApplication"`,
 * o no se usa y nada de esto existe.
 */
class InventarioApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}