package mx.tec.inventario.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Las consultas. Room escribe la implementación a partir de estas firmas.
 *
 * Fíjate en la diferencia entre las dos mitades:
 *   - Leer devuelve `Flow` y NO es suspend: no es "dame la lista una vez",
 *     es "avísame cada vez que esta consulta cambie de resultado".
 *   - Escribir es `suspend`: tarda, y no puede ocurrir en el hilo principal.
 */
@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos ORDER BY nombre COLLATE NOCASE ASC")
    fun observarTodos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE id = :id")
    fun observarPorId(id: Int): Flow<ProductoEntity?>

    @Insert
    suspend fun insertar(producto: ProductoEntity)

    @Update
    suspend fun actualizar(producto: ProductoEntity)

    @Delete
    suspend fun borrar(producto: ProductoEntity)
}