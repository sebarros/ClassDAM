package cl.duoc.aquacheck.ui.screens

// ARCHIVO: Pantalla de historial con búsqueda y filtros.

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.aquacheck.model.Resultado
import cl.duoc.aquacheck.ui.components.AquaTopBar
import cl.duoc.aquacheck.ui.components.BarraInferior
import cl.duoc.aquacheck.ui.components.RegistroCard
import cl.duoc.aquacheck.viewmodel.HistorialViewModel

@Composable
fun HistorialScreen(
    onInicio: () -> Unit,
    onDetalle: (Long) -> Unit,
    vm: HistorialViewModel = viewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { AquaTopBar("Historial") },
        bottomBar = { BarraInferior(seleccion = 1, onInicio = onInicio, onHistorial = {}) }
    ) { padding ->
        Column(Modifier.padding(padding).padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = state.busqueda,
                onValueChange = vm::buscar,
                label = { Text("Buscar por buzo, centro o fecha") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()).padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(selected = state.filtro == null, onClick = { vm.filtrar(null) }, label = { Text("Todos") })
                Resultado.entries.forEach { r ->
                    FilterChip(
                        selected = state.filtro == r,
                        onClick = { vm.filtrar(r) },
                        label = { Text(r.etiqueta) }
                    )
                }
            }
            if (state.visibles.isEmpty()) {
                Text("No hay registros que coincidan.", modifier = Modifier.padding(top = 16.dp))
            }
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.visibles, key = { it.id }) { registro ->
                    RegistroCard(registro) { onDetalle(registro.id) }
                }
            }
        }
    }
}
