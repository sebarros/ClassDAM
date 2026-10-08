package cl.duoc.aquacheck.data.local

// ARCHIVO: DAO: consultas y operaciones SQL sobre pre-chequeos e ítems.

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cl.duoc.aquacheck.model.EstadoItem
import cl.duoc.aquacheck.model.ItemChequeo
import cl.duoc.aquacheck.model.PreChequeo
import kotlinx.coroutines.flow.Flow

@Dao
interface PreChequeoDao {

    // region: consultas
    @Query("SELECT * FROM pre_chequeos ORDER BY id DESC")
    fun observarTodos(): Flow<List<PreChequeo>>

    @Query("SELECT * FROM pre_chequeos WHERE id = :id")
    fun observar(id: Long): Flow<PreChequeo?>

    @Query("SELECT * FROM items_chequeo WHERE preChequeoId = :id ORDER BY id")
    fun observarItems(id: Long): Flow<List<ItemChequeo>>
    // endregion

    // region: escritura
    @Insert
    suspend fun insertar(preChequeo: PreChequeo): Long

    @Insert
    suspend fun insertarItems(items: List<ItemChequeo>)

    @Update
    suspend fun actualizar(preChequeo: PreChequeo)

    @Query("UPDATE items_chequeo SET estado = :estado WHERE id = :itemId")
    suspend fun actualizarEstado(itemId: Long, estado: EstadoItem)

    @Query("UPDATE items_chequeo SET observacion = :texto WHERE id = :itemId")
    suspend fun actualizarObservacion(itemId: Long, texto: String)

    @Query("UPDATE items_chequeo SET fotoUri = :foto WHERE id = :itemId")
    suspend fun actualizarFoto(itemId: Long, foto: String)

    @Query("DELETE FROM pre_chequeos WHERE id = :id")
    suspend fun eliminar(id: Long)
    // endregion
}
