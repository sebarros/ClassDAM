package cl.duoc.aquacheck.util

// ARCHIVO: Plantilla del checklist y datos ficticios (centros, buzos, usuarios).

import cl.duoc.aquacheck.model.Rol
import cl.duoc.aquacheck.model.Usuario

/** Un ítem de la plantilla del checklist (basado en equipo de buceo intermedio, 36 m). */
data class PlantillaItem(val categoria: String, val nombre: String, val critico: Boolean = false)

object ChecklistBase {
    private const val EQUIPO = "Equipamiento del buzo"
    private const val AIRE = "Compresor y suministro de aire"
    private const val EMERGENCIA = "Emergencia"
    private const val ENTORNO = "Entorno y señalización"

    val items = listOf(
        PlantillaItem(EQUIPO, "Máscara facial con comunicaciones", critico = true),
        PlantillaItem(EQUIPO, "Regulador", critico = true),
        PlantillaItem(EQUIPO, "Botella de emergencia", critico = true),
        PlantillaItem(EQUIPO, "Arnés de escape rápido", critico = true),
        PlantillaItem(EQUIPO, "Cinturón de lastro con hebilla de escape", critico = true),
        PlantillaItem(EQUIPO, "Umbilical (aire + comunicaciones)", critico = true),
        PlantillaItem(EQUIPO, "Profundímetro y reloj de buceo"),
        PlantillaItem(EQUIPO, "Traje, aletas y cuchillo"),
        PlantillaItem(AIRE, "Compresor operativo", critico = true),
        PlantillaItem(AIRE, "Filtros Kaeser en buen estado", critico = true),
        PlantillaItem(AIRE, "Filtros de toma de aire del compresor"),
        PlantillaItem(AIRE, "Consola / manifold"),
        PlantillaItem(EMERGENCIA, "Buzo de emergencia disponible y equipado", critico = true),
        PlantillaItem(EMERGENCIA, "Oxígeno de contingencia disponible", critico = true),
        PlantillaItem(ENTORNO, "Señalización visible para embarcaciones", critico = true),
        PlantillaItem(ENTORNO, "Comunicaciones probadas"),
        PlantillaItem(ENTORNO, "Condiciones del mar y clima aptas")
    )
}

/** Datos ficticios para probar la app (no usar datos reales de personas). */
object DatosDemo {
    val centros = listOf("Centro Demo Calbuco", "Centro Demo Puerto Montt", "Centro Demo Chiloé")
    val buzos = listOf("Buzo Demo 01", "Buzo Demo 02", "Buzo Demo 03", "Buzo Demo 04")
    val usuarios = listOf(
        Usuario("Supervisor Demo", "supervisor", "1234", Rol.SUPERVISOR),
        Usuario("Jefe de Centro Demo", "admin", "1234", Rol.ADMIN)
    )
}
