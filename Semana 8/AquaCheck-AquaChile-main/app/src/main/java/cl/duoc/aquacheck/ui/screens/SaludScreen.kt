package cl.duoc.aquacheck.ui.screens

// ARCHIVO: Parámetros de salud ficticios y encuesta preventiva con semáforo en vivo.

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.aquacheck.ui.components.AquaTopBar
import cl.duoc.aquacheck.ui.components.CampoTexto
import cl.duoc.aquacheck.ui.components.color
import cl.duoc.aquacheck.util.Reglas
import cl.duoc.aquacheck.viewmodel.PreChequeoViewModel

@Composable
fun SaludScreen(
    id: Long,
    onVolver: () -> Unit,
    onContinuar: () -> Unit,
    vm: PreChequeoViewModel = viewModel()
) {
    LaunchedEffect(id) { vm.cargar(id) }
    val state by vm.uiState.collectAsStateWithLifecycle()

    var sistolica by rememberSaveable { mutableStateOf("") }
    var diastolica by rememberSaveable { mutableStateOf("") }
    var saturacion by rememberSaveable { mutableStateOf("") }
    var pulso by rememberSaveable { mutableStateOf("") }
    var sintomas by rememberSaveable { mutableStateOf(false) }
    var malDescanso by rememberSaveable { mutableStateOf(false) }
    var medicamentos by rememberSaveable { mutableStateOf(false) }

    // Si el supervisor vuelve a esta pantalla, recuperamos lo ya guardado
    val registro = state.registro
    LaunchedEffect(registro?.id, registro?.saludRegistrada) {
        if (registro != null && registro.saludRegistrada && sistolica.isBlank()) {
            sistolica = registro.sistolica?.toString().orEmpty()
            diastolica = registro.diastolica?.toString().orEmpty()
            saturacion = registro.saturacion?.toString().orEmpty()
            pulso = registro.pulso?.toString().orEmpty()
            sintomas = registro.sintomas
            malDescanso = registro.malDescanso
            medicamentos = registro.medicamentos
        }
    }

    // Semáforo en vivo: solo si los números son válidos
    val nivel = if (Reglas.validarSalud(sistolica, diastolica, saturacion, pulso).isEmpty()) {
        Reglas.nivelSalud(
            sistolica.trim().toInt(), diastolica.trim().toInt(), saturacion.trim().toInt(), pulso.trim().toInt(),
            sintomas, malDescanso, medicamentos
        )
    } else null

    Scaffold(topBar = { AquaTopBar("Salud del buzo (ficticia)", onVolver) }) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Parámetros medidos en terreno", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CampoTexto(sistolica, { sistolica = it }, "Sistólica", Modifier.weight(1f), state.errores["sistolica"], KeyboardType.Number)
                CampoTexto(diastolica, { diastolica = it }, "Diastólica", Modifier.weight(1f), state.errores["diastolica"], KeyboardType.Number)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CampoTexto(saturacion, { saturacion = it }, "Saturación O₂ (%)", Modifier.weight(1f), state.errores["saturacion"], KeyboardType.Number)
                CampoTexto(pulso, { pulso = it }, "Pulso (lat/min)", Modifier.weight(1f), state.errores["pulso"], KeyboardType.Number)
            }

            Text("Encuesta preventiva", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
            Pregunta("¿Presenta síntomas (mareo, dolor de oído, fiebre)?", sintomas) { sintomas = it }
            Pregunta("¿Durmió mal o descansó menos de 6 horas?", malDescanso) { malDescanso = it }
            Pregunta("¿Consumió alcohol o medicamentos en las últimas 24 h?", medicamentos) { medicamentos = it }

            if (nivel != null) {
                Card(colors = CardDefaults.cardColors(containerColor = nivel.color().copy(alpha = 0.15f))) {
                    Text(
                        "Semáforo de salud: ${nivel.etiqueta}",
                        color = nivel.color(),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
            Text(
                "Valores y umbrales ficticios con fines académicos. No constituye diagnóstico médico.",
                style = MaterialTheme.typography.bodySmall
            )
            Button(
                onClick = {
                    vm.guardarSalud(sistolica, diastolica, saturacion, pulso, sintomas, malDescanso, medicamentos, onContinuar)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar y ver resultado")
            }
        }
    }
}

@Composable
private fun Pregunta(texto: String, valor: Boolean, onCambio: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(texto, modifier = Modifier.weight(1f))
        Switch(checked = valor, onCheckedChange = onCambio)
    }
}
