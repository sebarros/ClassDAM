package cl.duoc.aquacheck.viewmodel

// ARCHIVO: ViewModel de sesión: valida el login y guarda el usuario y su rol.

import androidx.lifecycle.ViewModel
import cl.duoc.aquacheck.model.Usuario
import cl.duoc.aquacheck.util.DatosDemo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SesionUiState(
    val usuario: Usuario? = null,
    val error: String? = null
)

class SesionViewModel : ViewModel() {

    // Privado y mutable: solo el ViewModel puede cambiar el estado.
    private val _uiState = MutableStateFlow(SesionUiState())

    // Público y de solo lectura: la pantalla únicamente observa.
    val uiState: StateFlow<SesionUiState> = _uiState.asStateFlow()

    fun login(usuario: String, clave: String) {
        if (usuario.isBlank() || clave.isBlank()) {
            _uiState.update { it.copy(error = "Ingresa tu usuario y clave") }
            return
        }
        val encontrado = DatosDemo.usuarios.firstOrNull {
            it.usuario == usuario.trim().lowercase() && it.clave == clave
        }
        if (encontrado == null) {
            _uiState.update { it.copy(error = "Usuario o clave incorrectos") }
        } else {
            _uiState.update { SesionUiState(usuario = encontrado) }
        }
    }

    fun cerrarSesion() {
        _uiState.update { SesionUiState() }
    }
}
