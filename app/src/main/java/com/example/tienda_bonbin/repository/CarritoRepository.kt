package com.example.tienda_bonbin.repository

// Ya no necesitas la importación de 'copy', la eliminamos.
import com.example.tienda_bonbin.data.CarritoItem
import com.example.tienda_bonbin.data.CarritoDao // Asegúrate que el import apunte a la subcarpeta 'dao'
import com.example.tienda_bonbin.data.CarritoItemInfo
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para gestionar las operaciones del carrito de compras.
 * Habla con el CarritoDao para persistir los datos en la base de datos.
 *
 * @param carritoDao El objeto de acceso a datos para la tabla carrito_items.
 */
class CarritoRepository(private val carritoDao: CarritoDao) {

    /**
     * Agrega un producto al carrito de un usuario.
     *
     * Si el producto ya está en el carrito, incrementa su cantidad en 1.
     * Si no está, lo inserta en la base de datos con cantidad 1.
     *
     * @param productoId El ID del producto que se va a agregar.
     * @param usuarioId El ID del usuario al que pertenece el carrito.
     */
    suspend fun agregarProducto(productoId: Int, usuarioId: Int) {
        // 1. Busca si el item ya existe en la base de datos.
        val itemExistente = carritoDao.obtenerItem(usuarioId, productoId)

        if (itemExistente != null) {
            // --- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
            // 2. Si ya existe, simplemente incrementamos la cantidad del objeto existente...
            itemExistente.cantidad++
            // ...y llamamos a la función `actualizar` del DAO.
            carritoDao.actualizar(itemExistente)
        } else {
            // 3. Si no existe, creamos uno nuevo y lo insertamos. Esta parte ya estaba bien.
            val nuevoItem = CarritoItem(usuarioId = usuarioId, productoId = productoId, cantidad = 1)
            carritoDao.insertar(nuevoItem)
        }
    }

    /**
     * Obtiene la lista de items del carrito con información detallada del producto.
     */
    fun obtenerItemsDelCarrito(usuarioId: Int): Flow<List<CarritoItemInfo>> {
        return carritoDao.obtenerItemsInfoDelCarrito(usuarioId)
    }

    // Aquí puedes añadir más funciones en el futuro.
}
