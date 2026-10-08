package cl.duoc.aquacheck.viewmodel

// ARCHIVO: ViewModel del flujo: crear, checklist, fotos, salud, resultado, post-chequeo y borrado.

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.aquacheck.AquaCheckApp
import cl.duoc.aquacheck.model.EstadoItem
import cl.duoc.aquacheck.model.ItemChequeo
import cl.duoc.aquacheck.model.PreChequeo
import cl.duoc.aquacheck.util.Almacenamiento
import cl.duoc.aquacheck.util.Reglas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class PreChequeoUiState(
    val cargando: Boolean = true,
    val registro: PreChequeo? = null,
    val items: List<ItemChequeo> = emptyList(),
    val errores: Map<String, String> = emptyMap(),
    val mensaje: String? = null
)

class PreChequeoViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = (app as AquaCheckApp).repositorio

    private val _uiState = MutableStateFlow(PreChequeoUiState())
    val uiState: StateFlow<PreChequeoUiState> = _uiState.asStateFlow()

    private var idCargado: Long? = null
    private var jobCarga: Job? = null
    private var creando = false

    // region: cargar
    /** Room es la fuente de verdad: cada pantalla carga el registro por id y se actualiza sola. */
    fun cargar(id: Long) {
        if (id == idCargado) return
        idCargado = id
        jobCarga?.cancel()
        jobCarga = viewModelScope.launch {
            combine(repo.registro(id), repo.items(id)) { registro, items -> registro to items }
                .collect { (registro, items) ->
                    _uiState.update { it.copy(cargando = false, registro = registro, items = items) }
                }
        }
    }

    fun mostrarMensaje(texto: String) = _uiState.update { it.copy(mensaje = texto) }
    fun consumirMensaje() = _uiState.update { it.copy(mensaje = null) }
    // endregion

    // region: crear
    fun crear(
        centro: String, fecha: String, hora: String, buzo: String, supervisor: String,
        onCreado: (Long) -> Unit
    ) {
        val errores = Reglas.validarFormulario(centro, fecha, hora, buzo)
        _uiState.update { it.copy(errores = errores) }
        if (errores.isNotEmpty() || creando) return
        creando = true
        viewModelScope.launch {
            val id = repo.crear(
                PreChequeo(centro = centro, fecha = fecha, hora = hora, buzo = buzo, supervisor = supervisor)
            )
            creando = false
            onCreado(id)
        }
    }
    // endregion

    // region: checklist
    fun cambiarEstado(itemId: Long, estado: EstadoItem) {
        viewModelScope.launch { repo.actualizarEstado(itemId, estado) }
    }

    fun cambiarObservacion(itemId: Long, texto: String) {
        viewModelScope.launch { repo.actualizarObservacion(itemId, texto) }
    }

    fun guardarFoto(itemId: Long, foto: String) {
        viewModelScope.launch { repo.actualizarFoto(itemId, foto) }
    }

    /** Copia la imagen de la galería a la carpeta de la app (en segundo plano) y la asocia al ítem. */
    fun importarFoto(itemId: Long, origen: Uri) {
        viewModelScope.launch {
            val copia = withContext(Dispatchers.IO) {
                runCatching { Almacenamiento.copiarDesdeGaleria(getApplication(), origen) }.getOrNull()
            }
            if (copia != null) repo.actualizarFoto(itemId, copia) else mostrarMensaje("No se pudo cargar la imagen")
        }
    }

    fun validarChecklist(onOk: () -> Unit) {
        val error = Reglas.errorChecklist(_uiState.value.items)
        if (error != null) mostrarMensaje(error) else onOk()
    }
    // endregion

    // region: salud
    fun guardarSalud(
        sistolica: String, diastolica: String, saturacion: String, pulso: String,
        sintomas: Boolean, malDescanso: Boolean, medicamentos: Boolean,
        onOk: () -> Unit
    ) {
        val errores = Reglas.validarSalud(sistolica, diastolica, saturacion, pulso)
        _uiState.update { it.copy(errores = errores) }
        if (errores.isNotEmpty()) return
        val actual = _uiState.value.registro ?: return
        viewModelScope.launch {
            repo.actualizar(
                actual.copy(
                    saludRegistrada = true,
                    sistolica = sistolica.trim().toInt(),
                    diastolica = diastolica.trim().toInt(),
                    saturacion = saturacion.trim().toInt(),
                    pulso = pulso.trim().toInt(),
                    sintomas = sintomas,
                    malDescanso = malDescanso,
                    medicamentos = medicamentos
                )
            )
            onOk()
        }
    }
    // endregion

    // region: cierre
    /** Calcula el resultado preliminar y cierra el pre-chequeo. */
    fun confirmar(onOk: () -> Unit) {
        val actual = _uiState.value.registro ?: return
        val items = _uiState.value.items
        val error = Reglas.errorChecklist(items)
            ?: if (!actual.saludRegistrada) "Falta registrar los parámetros de salud" else null
        if (error != null) {
            mostrarMensaje(error)
            return
        }
        val resultado = Reglas.calcularResultado(items, Reglas.nivelDe(actual))
        viewModelScope.launch {
            repo.actualizar(actual.copy(resultado = resultado, cerrado = true))
            onOk()
        }
    }

    fun guardarPost(condicion: String, saturacion: String, observacion: String, onOk: () -> Unit) {
        val errores = Reglas.validarPost(condicion, saturacion, observacion)
        _uiState.update { it.copy(errores = errores) }
        if (errores.isNotEmpty()) return
        val actual = _uiState.value.registro ?: return
        viewModelScope.launch {
            repo.actualizar(
                actual.copy(
                    postCompletado = true,
                    postCondicion = condicion,
                    postSaturacion = saturacion.trim().toIntOrNull(),
                    postObservacion = observacion.trim()
                )
            )
            onOk()
        }
    }

    fun eliminar(onOk: () -> Unit) {
        val id = _uiState.value.registro?.id ?: return
        viewModelScope.launch {
            repo.eliminar(id)
            onOk()
        }
    }
    // endregion
}
