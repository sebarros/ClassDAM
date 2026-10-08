package cl.duoc.aquacheck.ui.screens

// ARCHIVO: Formulario para crear un pre-chequeo (centro, fecha, hora, buzo).

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.aquacheck.model.Usuario
import cl.duoc.aquacheck.ui.components.AquaTopBar
import cl.duoc.aquacheck.ui.components.CampoTexto
import cl.duoc.aquacheck.ui.components.Selector
import cl.duoc.aquacheck.util.DatosDemo
import cl.duoc.aquacheck.util.Reglas
import cl.duoc.aquacheck.viewmodel.PreChequeoViewModel

@Composable
fun NuevoPreChequeoScreen(
    usuario: Usuario,
    onVolver: () -> Unit,
    onCreado: (Long) -> Unit,
    vm: PreChequeoViewModel = viewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    var centro by rememberSaveable { mutableStateOf("") }
    var buzo by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf(Reglas.fechaHoy()) }
    var hora by rememberSaveable { mutableStateOf(Reglas.horaAhora()) }

    Scaffold(topBar = { AquaTopBar("Nuevo pre-chequeo", onVolver) }) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Selector("Centro de operación", DatosDemo.centros, centro, { centro = it }, state.errores["centro"])
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CampoTexto(fecha, { fecha = it }, "Fecha", Modifier.weight(1f), state.errores["fecha"])
                CampoTexto(hora, { hora = it }, "Hora", Modifier.weight(1f), state.errores["hora"])
            }
            Selector("Buzo (ficticio)", DatosDemo.buzos, buzo, { buzo = it }, state.errores["buzo"])
            OutlinedTextField(
                value = usuario.nombre,
                onValueChange = {},
                readOnly = true,
                label = { Text("Supervisor responsable") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { vm.crear(centro, fecha, hora, buzo, usuario.nombre, onCreado) },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Crear y comenzar checklist")
            }
        }
    }
}
