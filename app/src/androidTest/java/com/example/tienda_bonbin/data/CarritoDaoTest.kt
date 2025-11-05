package com.example.tienda_bonbin.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

// Importamos todas las clases de datos que vamos a usar en el test
import com.example.tienda_bonbin.data.CarritoItem
import com.example.tienda_bonbin.data.CarritoItemInfo
import com.example.tienda_bonbin.data.Producto
import com.example.tienda_bonbin.data.Usuario

@RunWith(AndroidJUnit4::class)
class CarritoDaoTest {

    private lateinit var carritoDao: CarritoDao
    private lateinit var productoDao: ProductoDao
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        carritoDao = db.carritoDao()
        productoDao = db.productoDao()
        usuarioDao = db.usuarioDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertarItem_y_obtenerInfoDelCarrito() = runBlocking {
        // 1. ARRANGE (Preparar)
        val usuario = Usuario(id = 1, nombre = "Test User", correo = "test@test.com", clave = "123", direccion = "Calle Falsa 123")
        val producto = Producto(id = 101, nombre = "Torta de Chocolate", precio = 25000.0, imagenUrl = "url_torta.jpg")

        // --- ↓↓↓ CORRECCIÓN FINAL Y CLAVE AQUÍ ↓↓↓ ---

        // 1. Usamos la función de UsuarioDao: `insertarUsuario`
        usuarioDao.insertarUsuario(usuario)

        // 2. Usamos la función de ProductoDao: `insertarTodos`, pasándole una lista
        productoDao.insertarTodos(listOf(producto))

        // --- ↑↑↑ FIN DE LA CORRECIÓN ↑↑↑ ---

        val carritoItem = CarritoItem(usuarioId = 1, productoId = 101, cantidad = 2)

        // 2. ACT (Actuar)
        // Usamos la función de CarritoDao: `insertar` (¡Asegúrate de que se llame así en tu CarritoDao!)
        carritoDao.insertar(carritoItem)

        // 3. ASSERT (Verificar)
        val itemsInfoRecuperados = carritoDao.obtenerItemsInfoDelCarrito(1).first()

        val itemInfoEsperado = CarritoItemInfo(
            productoId = 101,
            nombre = "Torta de Chocolate",
            precio = 25000.0,
            imagenUrl = "url_torta.jpg",
            cantidad = 2
        )

        assertEquals(1, itemsInfoRecuperados.size)
        assertEquals(itemInfoEsperado, itemsInfoRecuperados[0])
    }
}
