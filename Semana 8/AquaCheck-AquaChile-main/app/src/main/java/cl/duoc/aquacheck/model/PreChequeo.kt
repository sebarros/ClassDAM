package cl.duoc.aquacheck.model

// ARCHIVO: Entidad Room: registro completo de un pre-chequeo (faena, salud, resultado, post-chequeo).

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Un registro de pre-chequeo. Un solo registro guarda todo el proceso:
 * datos de la faena, parámetros de salud ficticios, resultado y post-chequeo.
 */
@Entity(tableName = "pre_chequeos")
data class PreChequeo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    // Datos de la faena
    val centro: String,
    val fecha: String,      // dd-MM-aaaa
    val hora: String,       // HH:mm
    val buzo: String,
    val supervisor: String,

    // Salud ficticia (se completa en la pantalla de salud)
    val saludRegistrada: Boolean = false,
    val sistolica: Int? = null,
    val diastolica: Int? = null,
    val saturacion: Int? = null,
    val pulso: Int? = null,
    val sintomas: Boolean = false,
    val malDescanso: Boolean = false,
    val medicamentos: Boolean = false,

    // Cierre del pre-chequeo
    val resultado: Resultado? = null,
    val cerrado: Boolean = false,

    // Post-chequeo
    val postCompletado: Boolean = false,
    val postCondicion: String = "",
    val postSaturacion: Int? = null,
    val postObservacion: String = ""
)
