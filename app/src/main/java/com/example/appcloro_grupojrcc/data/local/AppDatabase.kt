package com.example.appcloro_grupojrcc.data.local
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MedicionEntity::class, AlertaEntity::class, AccionEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicionDao(): MedicionDao
    abstract fun alertaDao(): AlertaDao
    abstract fun accionDao(): AccionDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        fun obtener(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cloro.db"
                ).fallbackToDestructiveMigration(true).build().also { instancia = it }
            }
    }
}