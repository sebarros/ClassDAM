package cl.duoc.aquacheck.util

// ARCHIVO: Reglas de negocio y validaciones puras (fáciles de probar con tests unitarios).

import cl.duoc.aquacheck.model.EstadoItem
import cl.duoc.aquacheck.model.ItemChequeo
import cl.duoc.aquacheck.model.NivelSalud
import cl.duoc.aquacheck.model.PreChequeo
import cl.duoc.aquacheck.model.Resultado
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

/**
 * Reglas de negocio y validaciones. Son funciones puras (no dependen de Android),
 * por eso se pueden probar con pruebas unitarias simples.
 * Los umbrales de salud son FICTICIOS, solo para el MVP académico.
 */
object Reglas {

    val FORMATO_FECHA: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT)
    private val FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm")

    fun fechaHoy(): String = LocalDate.now().format(FORMATO_FECHA)
    fun horaAhora(): String = LocalTime.now().format(FORMATO_HORA)

    // region: formulario
    fun validarFormulario(centro: String, fecha: String, hora: String, buzo: String): Map<String, String> {
        val errores = mutableMapOf<String, String>()
        if (centro.isBlank()) errores["centro"] = "Selecciona un centro"
        if (buzo.isBlank()) errores["buzo"] = "Selecciona un buzo"
        if (!fechaValida(fecha)) errores["fecha"] = "Usa el formato dd-MM-aaaa (ej. 15-10-2026)"
        if (!horaValida(hora)) errores["hora"] = "Usa el formato HH:mm (ej. 08:30)"
        return errores
    }

    private fun fechaValida(texto: String) =
        try { LocalDate.parse(texto, FORMATO_FECHA); true } catch (e: DateTimeParseException) { false }

    private fun horaValida(texto: String) =
        try { LocalTime.parse(texto, FORMATO_HORA); true } catch (e: DateTimeParseException) { false }
    // endregion

    // region: salud
    fun validarSalud(sistolica: String, diastolica: String, saturacion: String, pulso: String): Map<String, String> {
        val errores = mutableMapOf<String, String>()
        rango(sistolica, 70, 250, "mmHg")?.let { errores["sistolica"] = it }
        rango(diastolica, 40, 150, "mmHg")?.let { errores["diastolica"] = it }
        rango(saturacion, 50, 100, "%")?.let { errores["saturacion"] = it }
        rango(pulso, 30, 220, "lat/min")?.let { errores["pulso"] = it }
        val s = sistolica.toIntOrNull()
        val d = diastolica.toIntOrNull()
        if (s != null && d != null && d >= s && "diastolica" !in errores) {
            errores["diastolica"] = "Debe ser menor que la sistólica"
        }
        return errores
    }

    private fun rango(texto: String, min: Int, max: Int, unidad: String): String? {
        val n = texto.trim().toIntOrNull() ?: return "Ingresa un número"
        return if (n in min..max) null else "Debe estar entre $min y $max $unidad"
    }

    fun nivelSalud(
        sistolica: Int, diastolica: Int, saturacion: Int, pulso: Int,
        sintomas: Boolean, malDescanso: Boolean, medicamentos: Boolean
    ): NivelSalud {
        val critico = saturacion < 92 || sistolica >= 160 || diastolica >= 100 ||
            pulso < 45 || pulso > 120 || sintomas
        if (critico) return NivelSalud.CRITICO
        val alerta = saturacion < 95 || sistolica >= 140 || diastolica >= 90 ||
            pulso < 55 || pulso > 100 || malDescanso || medicamentos
        return if (alerta) NivelSalud.ALERTA else NivelSalud.OK
    }

    /** Nivel de salud de un registro, o null si aún no se registró la salud. */
    fun nivelDe(p: PreChequeo): NivelSalud? {
        if (!p.saludRegistrada) return null
        return nivelSalud(
            p.sistolica ?: return null, p.diastolica ?: return null,
            p.saturacion ?: return null, p.pulso ?: return null,
            p.sintomas, p.malDescanso, p.medicamentos
        )
    }
    // endregion

    // region: checklist
    /** Devuelve el primer problema que impide continuar, o null si el checklist está completo. */
    fun errorChecklist(items: List<ItemChequeo>): String? {
        val pendientes = items.count { it.estado == EstadoItem.PENDIENTE }
        if (pendientes > 0) return "Faltan $pendientes ítems por evaluar"
        val sinObservacion = items.firstOrNull {
            (it.estado == EstadoItem.OBSERVADO || it.estado == EstadoItem.NO_CUMPLE) &&
                it.observacion.trim().length < 5
        }
        if (sinObservacion != null) {
            return "Escribe una observación (mín. 5 letras) en: ${sinObservacion.nombre}"
        }
        return null
    }

    fun calcularResultado(items: List<ItemChequeo>, salud: NivelSalud?): Resultado {
        val falloCritico = items.any { it.critico && it.estado == EstadoItem.NO_CUMPLE }
        if (falloCritico || salud == NivelSalud.CRITICO) return Resultado.REQUIERE_REVISION
        val hayObservaciones = items.any {
            it.estado == EstadoItem.OBSERVADO || it.estado == EstadoItem.NO_CUMPLE
        } || salud == NivelSalud.ALERTA
        return if (hayObservaciones) Resultado.OBSERVADO else Resultado.CUMPLE
    }

    fun alertas(items: List<ItemChequeo>, salud: NivelSalud?): List<String> {
        val lista = mutableListOf<String>()
        items.filter { it.estado == EstadoItem.NO_CUMPLE }.forEach {
            lista += "No cumple${if (it.critico) " (crítico)" else ""}: ${it.nombre}"
        }
        items.filter { it.estado == EstadoItem.OBSERVADO }.forEach { lista += "Observado: ${it.nombre}" }
        when (salud) {
            NivelSalud.CRITICO -> lista += "Salud: parámetros en rango crítico"
            NivelSalud.ALERTA -> lista += "Salud: parámetros que requieren atención"
            else -> Unit
        }
        return lista
    }
    // endregion

    // region: post
    val CONDICIONES_POST = listOf("Sin novedad", "Molestias leves", "Requiere atención")

    fun validarPost(condicion: String, saturacion: String, observacion: String): Map<String, String> {
        val errores = mutableMapOf<String, String>()
        if (condicion.isBlank()) errores["condicion"] = "Selecciona la condición final"
        if (saturacion.isNotBlank()) rango(saturacion, 50, 100, "%")?.let { errores["saturacion"] = it }
        if (condicion.isNotBlank() && condicion != CONDICIONES_POST.first() && observacion.trim().length < 5) {
            errores["observacion"] = "Describe lo ocurrido (mín. 5 letras)"
        }
        return errores
    }
    // endregion
}
