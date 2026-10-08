package cl.duoc.aquacheck.data.local

// ARCHIVO: Base de datos Room (SQLite) con acceso único a toda la app.

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cl.duoc.aquacheck.model.ItemChequeo
import cl.duoc.aquacheck.model.PreChequeo

@Database(
    entities = [PreChequeo::class, ItemChequeo::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AquaCheckDatabase : RoomDatabase() {

    abstract fun dao(): PreChequeoDao

    companion object {
        @Volatile
        private var instancia: AquaCheckDatabase? = null

        fun obtener(context: Context): AquaCheckDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AquaCheckDatabase::class.java,
                    "aquacheck.db"
                ).build().also { instancia = it }
            }
    }
}
