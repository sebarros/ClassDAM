package com.duoc.clase6stateflow.viewmodel

import androidx.lifecycle.ViewModel
import com.duoc.clase6stateflow.model.OrdenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OrdenViewModel : ViewModel() {

    // 3. Estado interno modificable solamente por el ViewModel.
    private val _uiState = MutableStateFlow(OrdenUiState())

    // 4. La pantalla puede observar el estado, pero no modificarlo.
    val uiState: StateFlow<OrdenUiState> = _uiState.asStateFlow()

    // 5. Cambiamos el estado cuando ocurre una acción.
    fun avanzarEstado() {

        _uiState.update { actual ->

            val nuevoEstado = when (actual.orden.estado) {
                "Pendiente" -> "En proceso"
                "En proceso" -> "Finalizada"
                else -> "Finalizada"
            }

            actual.copy(
                orden = actual.orden.copy(
                    estado = nuevoEstado
                ),
                mensaje = "Estado actualizado a $nuevoEstado"
            )
        }
    }

    // 6. También podemos volver al estado inicial.
    fun reiniciar() {

        _uiState.value = OrdenUiState()
    }
}