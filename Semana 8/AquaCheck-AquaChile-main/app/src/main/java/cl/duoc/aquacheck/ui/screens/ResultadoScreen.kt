package cl.duoc.aquacheck.ui.screens

// ARCHIVO: Resultado preliminar (Cumple, Observado o Requiere revisión) y confirmación.

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.aquacheck.model.Resultado
import cl.duoc.aquacheck.ui.components.AquaTopBar
import cl.duoc.aquacheck.ui.components.DialogoConfirmar
import cl.duoc.aquacheck.ui.components.color
import cl.duoc.aquacheck.util.Reglas
import cl.duoc.aquacheck.viewmodel.PreChequeoViewModel

@Composable
fun ResultadoScreen(
    id: Long,
    onVolver: () -> Unit,
    onConfirmado: () -> Unit,
    vm: PreChequeoViewModel = viewModel()
) {
    LaunchedEffect(id) { vm.cargar(id) }
    val state by vm.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    var confirmando by remember { mutableStateOf(false) }

    LaunchedEffect(state.mensaje) {
        state.mensaje?.let {
            snackbar.showSnackbar(it)
            vm.consumirMensaje()
        }
    }

    val registro = state.registro
    val nivel = registro?.let { Reglas.nivelDe(it) }
    val resultado = Reglas.calcularResultado(state.items, nivel)
    val alertas = Reglas.alertas(state.items, nivel)

    Scaffold(
        topBar = { AquaTopBar("Resultado preliminar", onVolver) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(colors = CardDefaults.cardColors(containerColor = resultado.color().copy(alpha = 0.15f))) {
                Column(
                    Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = when (resultado) {
                            Resultado.CUMPLE -> Icons.Default.CheckCircle
                            Resultado.OBSERVADO -> Icons.Default.Warning
                            Resultado.REQUIERE_REVISION -> Icons.Default.Error
                        },
                        contentDescription = null,
                        tint = resultado.color(),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        resultado.etiqueta.uppercase(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = resultado.color()
                    )
                }
            }

            if (alertas.isEmpty()) {
                Text("Sin alertas: todos los ítems cumplen y la salud está en rango normal.")
            } else {
                Text("Alertas", style = MaterialTheme.typography.titleMedium)
                alertas.forEach { Text("• $it") }
            }
            Text(
                "Resultado de apoyo para el supervisor. No es una autorización oficial de faena.",
                style = MaterialTheme.typography.bodySmall
            )

            Button(onClick = { confirmando = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Confirmar y guardar")
            }
            OutlinedButton(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
                Text("Volver a revisar")
            }
        }
    }

    if (confirmando) {
        DialogoConfirmar(
            titulo = "Guardar pre-chequeo",
            texto = "Se cerrará el registro con resultado «${resultado.etiqueta}».",
            onConfirmar = {
                confirmando = false
                vm.confirmar(onConfirmado)
            },
            onCancelar = { confirmando = false }
        )
    }
}
