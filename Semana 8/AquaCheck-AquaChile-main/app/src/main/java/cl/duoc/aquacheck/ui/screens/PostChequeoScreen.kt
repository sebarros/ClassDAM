package cl.duoc.aquacheck.ui.screens

// ARCHIVO: Registro del post-chequeo al terminar la inmersión.

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.aquacheck.ui.components.AquaTopBar
import cl.duoc.aquacheck.ui.components.CampoTexto
import cl.duoc.aquacheck.ui.components.Selector
import cl.duoc.aquacheck.util.Reglas
import cl.duoc.aquacheck.viewmodel.PreChequeoViewModel

@Composable
fun PostChequeoScreen(
    id: Long,
    onVolver: () -> Unit,
    onGuardado: () -> Unit,
    vm: PreChequeoViewModel = viewModel()
) {
    LaunchedEffect(id) { vm.cargar(id) }
    val state by vm.uiState.collectAsStateWithLifecycle()
    var condicion by rememberSaveable { mutableStateOf("") }
    var saturacion by rememberSaveable { mutableStateOf("") }
    var observacion by rememberSaveable { mutableStateOf("") }

    Scaffold(topBar = { AquaTopBar("Post-chequeo", onVolver) }) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Registra cómo terminó la inmersión de ${state.registro?.buzo ?: "..."}.",
                style = MaterialTheme.typography.bodyLarge
            )
            Selector("Condición final del buzo", Reglas.CONDICIONES_POST, condicion, { condicion = it }, state.errores["condicion"])
            CampoTexto(saturacion, { saturacion = it }, "SpO₂ final % (opcional)", error = state.errores["saturacion"], teclado = KeyboardType.Number)
            CampoTexto(observacion, { observacion = it }, "Observaciones", error = state.errores["observacion"], lineas = 3)
            Button(
                onClick = { vm.guardarPost(condicion, saturacion, observacion, onGuardado) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar post-chequeo")
            }
        }
    }
}
