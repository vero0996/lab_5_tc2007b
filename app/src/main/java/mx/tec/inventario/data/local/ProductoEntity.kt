package mx.tec.inventario.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Una fila de la tabla `productos`.
 *
 * No es el dominio: es la forma que tienen los datos DENTRO de la base.
 * Room solo sabe de esta clase; `Producto` no lleva una sola anotación.
 */
@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val precio: Double,
    val cantidad: Int
)