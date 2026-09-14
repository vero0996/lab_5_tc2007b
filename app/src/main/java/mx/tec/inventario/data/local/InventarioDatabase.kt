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
@Database(entities = [ProductoEntity::class], version = 2, exportSchema = false)
abstract class InventarioDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao

    companion object {

        @Volatile
        private var instancia: InventarioDatabase? = null

        fun obtener(context: Context): InventarioDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    InventarioDatabase::class.java,
                    "inventario.db"
                ).fallbackToDestructiveMigration(dropAllTables = true)
                    .build().also { instancia = it }
            }
    }
}