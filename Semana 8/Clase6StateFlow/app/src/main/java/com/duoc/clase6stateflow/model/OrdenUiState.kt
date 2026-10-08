package com.duoc.clase6stateflow.model

data class OrdenUiState(
    val orden: OrdenTrabajo = OrdenTrabajo(
        id = 1,
        titulo = "Revisión de equipo",
        estado = "Pendiente"
    ),
    val mensaje: String = "Orden lista para comenzar"
)