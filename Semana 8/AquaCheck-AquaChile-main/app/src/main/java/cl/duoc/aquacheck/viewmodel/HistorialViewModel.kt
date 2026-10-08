package cl.duoc.aquacheck.viewmodel

// ARCHIVO: ViewModel del historial: lista de registros con búsqueda y filtro por resultado.

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.aquacheck.AquaCheckApp
import cl.duoc.aquacheck.model.PreChequeo
import cl.duoc.aquacheck.model.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistorialUiState(
    val registros: List<PreChequeo> = emptyList(),
    val busqueda: String = "",
    val filtro: Resultado? = null
) {
    /** Lista ya filtrada por texto (buzo, centro o fecha) y por resultado. */
    val visibles: List<PreChequeo>
        get() = registros.filter { r ->
            val coincideResultado = filtro == null || r.resultado == filtro
            val texto = busqueda.trim()
            val coincideTexto = texto.isEmpty() ||
                listOf(r.buzo, r.centro, r.fecha).any { it.contains(texto, ignoreCase = true) }
            coincideResultado && coincideTexto
        }
}

class HistorialViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = (app as AquaCheckApp).repositorio

    private val _uiState = MutableStateFlow(HistorialUiState())
    val uiState: StateFlow<HistorialUiState> = _uiState.asStateFlow()

    init {
        // Room emite una lista nueva cada vez que cambia la tabla.
        viewModelScope.launch {
            repo.todos().collect { lista -> _uiState.update { it.copy(registros = lista) } }
        }
    }

    fun buscar(texto: String) = _uiState.update { it.copy(busqueda = texto) }
    fun filtrar(resultado: Resultado?) = _uiState.update { it.copy(filtro = resultado) }
}
