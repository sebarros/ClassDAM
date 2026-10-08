package cl.duoc.aquacheck.data.repository

// ARCHIVO: Repositorio: único acceso a los datos; crea el pre-chequeo con sus ítems.

import androidx.room.withTransaction
import cl.duoc.aquacheck.data.local.AquaCheckDatabase
import cl.duoc.aquacheck.model.EstadoItem
import cl.duoc.aquacheck.model.ItemChequeo
import cl.duoc.aquacheck.model.PreChequeo
import cl.duoc.aquacheck.util.ChecklistBase

/** Único punto de acceso a los datos: los ViewModels no conocen Room. */
class PreChequeoRepository(private val db: AquaCheckDatabase) {

    private val dao = db.dao()

    fun todos() = dao.observarTodos()
    fun registro(id: Long) = dao.observar(id)
    fun items(id: Long) = dao.observarItems(id)

    /** Crea el pre-chequeo junto con todos los ítems de la plantilla (en una transacción). */
    suspend fun crear(preChequeo: PreChequeo): Long = db.withTransaction {
        val id = dao.insertar(preChequeo)
        dao.insertarItems(
            ChecklistBase.items.map {
                ItemChequeo(preChequeoId = id, categoria = it.categoria, nombre = it.nombre, critico = it.critico)
            }
        )
        id
    }

    suspend fun actualizar(preChequeo: PreChequeo) = dao.actualizar(preChequeo)
    suspend fun actualizarEstado(itemId: Long, estado: EstadoItem) = dao.actualizarEstado(itemId, estado)
    suspend fun actualizarObservacion(itemId: Long, texto: String) = dao.actualizarObservacion(itemId, texto)
    suspend fun actualizarFoto(itemId: Long, foto: String) = dao.actualizarFoto(itemId, foto)
    suspend fun eliminar(id: Long) = dao.eliminar(id)
}
