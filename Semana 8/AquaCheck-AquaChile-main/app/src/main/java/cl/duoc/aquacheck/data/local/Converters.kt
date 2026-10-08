package cl.duoc.aquacheck.data.local

// ARCHIVO: Convierte enums a texto para que Room pueda guardarlos.

import androidx.room.TypeConverter
import cl.duoc.aquacheck.model.EstadoItem
import cl.duoc.aquacheck.model.Resultado

/** Room solo guarda tipos simples; los enum se guardan como texto. */
class Converters {
    @TypeConverter
    fun deEstado(estado: EstadoItem): String = estado.name

    @TypeConverter
    fun aEstado(texto: String): EstadoItem = EstadoItem.valueOf(texto)

    @TypeConverter
    fun deResultado(resultado: Resultado?): String? = resultado?.name

    @TypeConverter
    fun aResultado(texto: String?): Resultado? = texto?.let { Resultado.valueOf(it) }
}
