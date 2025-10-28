package com.example.tienda_bonbin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Usuario::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Esta función abstracta le dice a Room qué DAOs existen en esta base de datos.
    // Room la implementará por nosotros.
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        // La anotación @Volatile asegura que el valor de INSTANCE
        // esté siempre actualizado y sea el mismo para todos los hilos de ejecución.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Esta función obtiene la instancia única (singleton) de la base de datos.
        // Si ya existe, la devuelve. Si no, la crea de forma segura.
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bonbin_database" // Nombre del archivo físico de la base de datos
                ).build()
                INSTANCE = instance
                // Devuelve la instancia recién creada
                instance
            }
        }
    }
}
