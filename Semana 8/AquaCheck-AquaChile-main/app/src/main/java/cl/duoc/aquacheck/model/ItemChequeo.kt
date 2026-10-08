package cl.duoc.aquacheck.model

// ARCHIVO: Entidad Room: un ítem del checklist TPR-24 con estado, observación y foto.

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Un ítem del checklist TPR-24 asociado a un pre-chequeo. */
@Entity(
    tableName = "items_chequeo",
    foreignKeys = [ForeignKey(
        entity = PreChequeo::class,
        parentColumns = ["id"],
        childColumns = ["preChequeoId"],
        onDelete = ForeignKey.CASCADE   // al borrar el pre-chequeo se borran sus ítems
    )],
    indices = [Index("preChequeoId")]
)
data class ItemChequeo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val preChequeoId: Long,
    val categoria: String,
    val nombre: String,
    val critico: Boolean,
    val estado: EstadoItem = EstadoItem.PENDIENTE,
    val observacion: String = "",
    val fotoUri: String = ""
)
