package mx.tec.inventario.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

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

        // @Volatile: si un hilo cambia esta referencia, los demás la ven al
        // instante. Sin esto, dos hilos podrían crear dos bases distintas.
        @Volatile
        private var instancia: InventarioDatabase? = null

        // 2. Defines el objeto Migration de la versión 1 a la 2
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE productos ADD COLUMN categoria TEXT NOT NULL DEFAULT 'General'"
                )
            }
        }

        fun obtener(context: Context): InventarioDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    InventarioDatabase::class.java,
                    "inventario.db"
                )
                    .addMigrations(MIGRATION_1_2) // 3. Le agregas la migración aquí
                    .build().also { instancia = it }
            }
    }
}