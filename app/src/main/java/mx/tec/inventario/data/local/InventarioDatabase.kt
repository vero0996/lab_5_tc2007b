package mx.tec.inventario.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * La base de datos. Es abstracta: Room genera la implementación en tiempo de
 * compilación, con KSP.
 *
 * `version` es el número que hay que subir cada vez que cambia la forma de una
 * tabla. `exportSchema = false` porque en esta práctica no versionamos el
 * esquema en el repositorio.
 */
@Database(entities = [ProductoEntity::class], version = 1, exportSchema = false)
abstract class InventarioDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao

    companion object {

        // @Volatile: si un hilo cambia esta referencia, los demás la ven al
        // instante. Sin esto, dos hilos podrían crear dos bases distintas.
        @Volatile
        private var instancia: InventarioDatabase? = null

        /**
         * Abrir la base es caro. Se hace UNA vez en toda la vida del proceso, y
         * a partir de ahí se reparte la misma instancia.
         */
        fun obtener(context: Context): InventarioDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    InventarioDatabase::class.java,
                    "inventario.db"
                ).build().also { instancia = it }
            }
    }
}