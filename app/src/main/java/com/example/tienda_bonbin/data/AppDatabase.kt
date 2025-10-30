package com.example.tienda_bonbin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tienda_bonbin.data.ProductoDao // Importa tu ProductoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Usuario::class,
        Producto::class,
        // Compra::class, // Comenta las entidades que aún no usas para evitar errores
        // DetalleCompra::class,
        CarritoItem::class
    ],
    version = 2, // Puedes mantener la versión o subirla a 3
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun carritoDao(): CarritoDao
    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bonbin_database"
                )
                    .fallbackToDestructiveMigration()
                    // --- 1. ¡LÍNEA AÑADIDA! ---
                    // Ahora sí le decimos a Room que use nuestro Callback.
                    .addCallback(AppDatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    // --- 2. CLASE DE CALLBACK DEFINIDA DENTRO DE AppDatabase ---
    // Esto es más limpio y mantiene toda la lógica de la BD junta.
    private class AppDatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Cuando la base de datos se crea por primera vez, poblamos los productos.
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    val productoDao = database.productoDao()
                    productoDao.insertarTodos(getInitialProducts())
                }
            }
        }

        // --- 3. LA LISTA DE PRODUCTOS, AHORA EN SU SITIO ---
        // Esta función ahora pertenece al Callback que la va a usar.
        private fun getInitialProducts(): List<Producto> {
            return listOf(
                Producto(id = 1, nombre = "Torta de Frutos Rojos", precio = 22000.0, imagenUrl = "https://tortasdelacasa.com/wp-content/uploads/2024/02/DSC4340-scaled.jpg"),
                Producto(id = 2, nombre = "Torta Chocolate Blanco", precio = 18500.0, imagenUrl = "https://images.aws.nestle.recipes/original/2024_10_23T06_40_18_badun_images.badun.es_tarta_fria_de_chocolate_blanco_con_frutas.jpg"),
                Producto(id = 3, nombre = "Pastel de Vainilla", precio = 12000.0, imagenUrl = "https://tortamaniaecuador.com/wp-content/uploads/2022/12/Vainilla-con-crema-pequena-300x300.png"),
                Producto(id = 4, nombre = "Torta de Ciruela", precio = 16990.0, imagenUrl = "https://rhenania.cl/wp-content/uploads/2020/12/CIRUELA-MANJAR-BLANCO.jpg"),
                Producto(id = 5, nombre = "Mousse de Chocolate", precio = 9500.0, imagenUrl = "https://www.elinasaiach.com/wp-content/uploads/2022/04/Mousse-Chocolate-3.jpg"),
                Producto(id = 6, nombre = "Tiramisú Italiano", precio = 15000.0, imagenUrl = "https://recetasdecocina.elmundo.es/wp-content/uploads/2022/08/tiramisu-postre-italiano.jpg"),
                Producto(id = 7, nombre = "Torta Panqueque Naranja", precio = 19990.0, imagenUrl = "https://www.lomismoperosano.cl/wp-content/uploads/2022/01/receta-torta-panqueque-naranja.jpg"),
                Producto(id = 8, nombre = "Tarta de Queso", precio = 14500.0, imagenUrl = "https://www.hola.com/horizon/landscape/64c21cd97107-tarta-horno-queso-t.jpg"),
                Producto(id = 9, nombre = "Pastel Imposible", precio = 21000.0, imagenUrl = "https://cdn7.kiwilimon.com/recetaimagen/1366/960x640/2229.jpg.jpg"),
                Producto(id = 10, nombre = "Tarta de Santiago", precio = 13000.0, imagenUrl = "https://jetextramar.com/wp-content/uploads/2021/11/tarta-de-santiago1-empresa-de-alimentos.jpg"),
                Producto(id = 11, nombre = "Brownie de Chocolate", precio = 8000.0, imagenUrl = "https://azucarledesma.com/wp-content/uploads/2024/01/20240131-BrownieLight.jpg"),
                Producto(id = 12, nombre = "Pan sin Gluten", precio = 5500.0, imagenUrl = "https://dinkel.es/wp-content/uploads/2020/06/1041-Pan-sin-Gluten-2.png"),
                Producto(id = 13, nombre = "Cheesecake Frutos Rojos", precio = 17800.0, imagenUrl = "https://luciapaula.com/wp-content/uploads/2023/01/Blog-1970-01-20-125839033.jpg"),
                Producto(id = 14, nombre = "Galletas de Avena", precio = 6000.0, imagenUrl = "https://i.blogs.es/8792e6/galletas-avea-tahina-datiles/840_560.jpg"),
                Producto(id = 15, nombre = "Alfajores de Maicena", precio = 7500.0, imagenUrl = "https://i.pinimg.com/originals/aa/a0/4f/aaa04fb61005c9fc6d748dee6eac76f3.jpg"),
                Producto(id = 16, nombre = "Donas Glaseadas", precio = 4500.0, imagenUrl = "https://i.pinimg.com/474x/3b/bc/bb/3bbcbb826b865e5278f53a5b2661c2e5.jpg")
            )
        }
    }
}
