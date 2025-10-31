package com.example.tienda_bonbin.repository

import androidx.room.Transaction
import com.example.tienda_bonbin.data.CarritoItemInfo
import com.example.tienda_bonbin.data.Compra
import com.example.tienda_bonbin.data.DetalleCompra
import com.example.tienda_bonbin.data.CompraDao
// --- 1. AÑADE ESTAS IMPORTACIONES PARA MANEJAR FECHAS COMO TEXTO ---
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CompraRepository(private val compraDao: CompraDao) {

    /**
     * Orquesta la creación de una compra completa a partir de los items del carrito.
     * Esta operación es transaccional: o todo tiene éxito, o nada se guarda.
     *
     * @param usuarioId El ID del usuario que realiza la compra.
     * @param items La lista de productos en el carrito.
     */
    @Transaction
    suspend fun crearCompraDesdeCarrito(usuarioId: Int, items: List<CarritoItemInfo>) {
        // 1. Calcular el total de la compra
        val totalCompra = items.sumOf { it.precio * it.cantidad }

        // --- 2. ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
        // Creamos un formateador de fecha para convertir la fecha actual a un String.
        // Puedes cambiar el formato "yyyy-MM-dd HH:mm:ss" al que prefieras.
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val fechaActualString = formatoFecha.format(Date())

        // 3. Crear el objeto Compra principal, ahora con la fecha como String.
        val nuevaCompra = Compra(
            usuarioId = usuarioId,
            fechaCompra = fechaActualString, // Usamos el String formateado
            total = totalCompra
        )

        // 4. Insertar la compra y obtener su ID
        val compraId = compraDao.insertarCompra(nuevaCompra)

        // 5. Crear la lista de detalles de la compra (esto no cambia)
        val detalles = items.map { item ->
            DetalleCompra(
                compraId = compraId.toInt(),
                productoId = item.productoId,
                cantidad = item.cantidad,
                precioUnitario = item.precio
            )
        }

        // 6. Insertar todos los detalles en la base de datos
        compraDao.insertarDetalles(detalles)
    }
}
