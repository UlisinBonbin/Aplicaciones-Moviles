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
        val usuario = Usuario(id = 1, nombre = "Test User", apellido = "Android", correo = "test@test.com", clave = "123", direccion = "Calle Falsa 123", rol = "USER")
        val producto = Producto(id = 101, nombre = "Torta de Chocolate", precio = 25000.0, imagenUrl = "url_torta.jpg")

        usuarioDao.insertarUsuario(usuario)

        productoDao.insertarTodos(listOf(producto))


        val carritoItem = CarritoItem(usuarioId = 1, productoId = 101, cantidad = 2)

        carritoDao.insertar(carritoItem)

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
