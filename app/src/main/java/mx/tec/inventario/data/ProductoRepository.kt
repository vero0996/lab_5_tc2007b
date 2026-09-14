package mx.tec.inventario.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import mx.tec.inventario.data.local.ProductoDao
import mx.tec.inventario.data.local.toDomain
import mx.tec.inventario.data.local.toEntity
import mx.tec.inventario.domain.Producto

/**
 * La única puerta a los datos. Hacia arriba habla de `Producto`; hacia abajo,
 * de `ProductoEntity`. Nadie fuera de `data/` sabe que existe Room.
 */
class ProductoRepository(private val dao: ProductoDao) {

    fun observarTodos(): Flow<List<Producto>> =
        dao.observarTodos().map { filas -> filas.map { it.toDomain() } }

    fun observarPorId(id: Int): Flow<Producto?> =
        dao.observarPorId(id).map { fila -> fila?.toDomain() }

    suspend fun agregar(producto: Producto) = dao.insertar(producto.toEntity())

    suspend fun actualizar(producto: Producto) = dao.actualizar(producto.toEntity())

    suspend fun borrar(producto: Producto) = dao.borrar(producto.toEntity())
}