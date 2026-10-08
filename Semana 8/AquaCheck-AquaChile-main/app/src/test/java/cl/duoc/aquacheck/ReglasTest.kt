package cl.duoc.aquacheck

// ARCHIVO: Pruebas unitarias de validaciones y reglas de negocio.

import cl.duoc.aquacheck.model.EstadoItem
import cl.duoc.aquacheck.model.ItemChequeo
import cl.duoc.aquacheck.model.NivelSalud
import cl.duoc.aquacheck.model.Resultado
import cl.duoc.aquacheck.util.Reglas
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ReglasTest {

    private fun item(estado: EstadoItem, critico: Boolean = false, obs: String = "") = ItemChequeo(
        preChequeoId = 1, categoria = "Test", nombre = "Ítem", critico = critico,
        estado = estado, observacion = obs
    )

    @Test
    fun formularioVacio_devuelveErroresDeLosCuatroCampos() {
        val errores = Reglas.validarFormulario("", "", "", "")
        assertEquals(setOf("centro", "fecha", "hora", "buzo"), errores.keys)
    }

    @Test
    fun formularioCorrecto_noTieneErrores() {
        val errores = Reglas.validarFormulario("Centro Demo", "15-10-2026", "08:30", "Buzo Demo 01")
        assertTrue(errores.isEmpty())
    }

    @Test
    fun saturacionBaja_esCritica_yNormal_esOk() {
        assertEquals(NivelSalud.CRITICO, Reglas.nivelSalud(120, 80, 90, 70, false, false, false))
        assertEquals(NivelSalud.ALERTA, Reglas.nivelSalud(120, 80, 93, 70, false, false, false))
        assertEquals(NivelSalud.OK, Reglas.nivelSalud(120, 80, 98, 70, false, false, false))
    }

    @Test
    fun checklist_exigeEvaluarTodoYObservacionEnObservados() {
        assertNotNull(Reglas.errorChecklist(listOf(item(EstadoItem.PENDIENTE))))
        assertNotNull(Reglas.errorChecklist(listOf(item(EstadoItem.OBSERVADO, obs = ""))))
        assertNull(Reglas.errorChecklist(listOf(item(EstadoItem.OBSERVADO, obs = "Filtro con desgaste"))))
    }

    @Test
    fun resultado_segunItemsYSalud() {
        val todoOk = listOf(item(EstadoItem.CUMPLE), item(EstadoItem.NO_APLICA))
        assertEquals(Resultado.CUMPLE, Reglas.calcularResultado(todoOk, NivelSalud.OK))
        assertEquals(Resultado.OBSERVADO, Reglas.calcularResultado(todoOk, NivelSalud.ALERTA))

        val fallaCritica = listOf(item(EstadoItem.NO_CUMPLE, critico = true, obs = "Sin oxígeno"))
        assertEquals(Resultado.REQUIERE_REVISION, Reglas.calcularResultado(fallaCritica, NivelSalud.OK))

        val fallaNormal = listOf(item(EstadoItem.NO_CUMPLE, critico = false, obs = "Aletas dañadas"))
        assertEquals(Resultado.OBSERVADO, Reglas.calcularResultado(fallaNormal, NivelSalud.OK))
    }
}
