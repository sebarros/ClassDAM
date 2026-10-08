package cl.duoc.aquacheck.ui.screens

// ARCHIVO: Detalle completo de un registro y acciones según rol y estado.

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.aquacheck.model.ItemChequeo
import cl.duoc.aquacheck.model.PreChequeo
import cl.duoc.aquacheck.model.Rol
import cl.duoc.aquacheck.ui.components.AquaTopBar
import cl.duoc.aquacheck.ui.components.DialogoConfirmar
import cl.duoc.aquacheck.ui.components.Insignia
import cl.duoc.aquacheck.ui.components.color
import cl.duoc.aquacheck.ui.theme.Gris
import cl.duoc.aquacheck.util.Reglas
import cl.duoc.aquacheck.viewmodel.PreChequeoViewModel
import coil.compose.AsyncImage

@Composable
fun DetalleScreen(
    id: Long,
    rol: Rol,
    onVolver: () -> Unit,
    onContinuar: () -> Unit,
    onPostChequeo: () -> Unit,
    onEliminado: () -> Unit,
    vm: PreChequeoViewModel = viewModel()
) {
    LaunchedEffect(id) { vm.cargar(id) }
    val state by vm.uiState.collectAsStateWithLifecycle()
    var eliminando by remember { mutableStateOf(false) }
    val registro = state.registro

    Scaffold(topBar = { AquaTopBar("Detalle del registro", onVolver) }) { padding ->
        if (registro == null) {
            Column(Modifier.padding(padding).fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                if (state.cargando) CircularProgressIndicator() else Text("Registro no encontrado")
            }
            return@Scaffold
        }
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { Cabecera(registro) }
            item { SaludResumen(registro) }
            if (registro.postCompletado) item { PostResumen(registro) }

            item { Text("Checklist", style = MaterialTheme.typography.titleMedium) }
            items(state.items, key = { it.id }) { ItemResumen(it) }

            // Acciones según rol y estado del registro (el Admin solo consulta)
            if (rol == Rol.SUPERVISOR) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (!registro.cerrado) {
                            Button(onClick = onContinuar, modifier = Modifier.fillMaxWidth()) { Text("Continuar pre-chequeo") }
                            OutlinedButton(onClick = { eliminando = true }, modifier = Modifier.fillMaxWidth()) {
                                Text("Eliminar borrador")
                            }
                        } else if (!registro.postCompletado) {
                            Button(onClick = onPostChequeo, modifier = Modifier.fillMaxWidth()) { Text("Registrar post-chequeo") }
                        }
                    }
                }
            }
        }
    }

    if (eliminando) {
        DialogoConfirmar(
            titulo = "Eliminar borrador",
            texto = "Se borrará este pre-chequeo y sus evidencias. ¿Continuar?",
            onConfirmar = {
                eliminando = false
                vm.eliminar(onEliminado)
            },
            onCancelar = { eliminando = false }
        )
    }
}

@Composable
private fun Cabecera(r: PreChequeo) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(r.buzo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                val resultado = r.resultado
                if (resultado != null) Insignia(resultado.etiqueta, resultado.color()) else Insignia("Borrador", Gris)
            }
            Text("Centro: ${r.centro}")
            Text("Fecha: ${r.fecha}  ${r.hora}")
            Text("Supervisor: ${r.supervisor}")
        }
    }
}

@Composable
private fun SaludResumen(r: PreChequeo) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Salud (ficticia)", style = MaterialTheme.typography.titleMedium)
            val nivel = Reglas.nivelDe(r)
            if (nivel == null) {
                Text("Aún no registrada")
            } else {
                Text("Presión ${r.sistolica}/${r.diastolica} mmHg · SpO₂ ${r.saturacion}% · Pulso ${r.pulso}")
                Text("Síntomas: ${if (r.sintomas) "Sí" else "No"} · Mal descanso: ${if (r.malDescanso) "Sí" else "No"} · Alcohol/medicamentos: ${if (r.medicamentos) "Sí" else "No"}")
                Insignia(nivel.etiqueta, nivel.color())
            }
        }
    }
}

@Composable
private fun PostResumen(r: PreChequeo) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Post-chequeo", style = MaterialTheme.typography.titleMedium)
            Text("Condición final: ${r.postCondicion}")
            r.postSaturacion?.let { Text("SpO₂ final: $it%") }
            if (r.postObservacion.isNotBlank()) Text("Observación: ${r.postObservacion}")
        }
    }
}

@Composable
private fun ItemResumen(item: ItemChequeo) {
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(Modifier.weight(1f)) {
                Text(item.nombre, fontWeight = FontWeight.SemiBold)
                if (item.observacion.isNotBlank()) Text(item.observacion, style = MaterialTheme.typography.bodySmall)
            }
            if (item.fotoUri.isNotBlank()) {
                AsyncImage(
                    model = item.fotoUri,
                    contentDescription = "Evidencia de ${item.nombre}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp))
                )
            }
            Insignia(item.estado.etiqueta, item.estado.color())
        }
    }
}
