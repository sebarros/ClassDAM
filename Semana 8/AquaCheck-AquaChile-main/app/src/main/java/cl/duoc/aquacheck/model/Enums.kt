package cl.duoc.aquacheck.model

// ARCHIVO: Enumeraciones del dominio: estado de ítem, resultado, nivel de salud y rol.

/** Estado de cada ítem del checklist. PENDIENTE = aún no evaluado. */
enum class EstadoItem(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    CUMPLE("Cumple"),
    OBSERVADO("Observado"),
    NO_CUMPLE("No cumple"),
    NO_APLICA("No aplica")
}

/** Resultado preliminar del pre-chequeo (el semáforo general). */
enum class Resultado(val etiqueta: String) {
    CUMPLE("Cumple"),
    OBSERVADO("Observado"),
    REQUIERE_REVISION("Requiere revisión")
}

/** Nivel de los parámetros de salud ficticios. */
enum class NivelSalud(val etiqueta: String) {
    OK("Normal"),
    ALERTA("Atención"),
    CRITICO("Crítico")
}

enum class Rol(val etiqueta: String) {
    SUPERVISOR("Supervisor de buceo"),
    ADMIN("Jefe de centro / Admin")
}
