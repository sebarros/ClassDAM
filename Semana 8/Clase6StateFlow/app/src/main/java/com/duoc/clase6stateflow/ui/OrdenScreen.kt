package com.duoc.clase6stateflow.ui
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.clase6stateflow.viewmodel.OrdenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdenScreen(
    vm: OrdenViewModel = viewModel()
) {

    // 7. Compose observa permanentemente nuestro StateFlow.
    val state by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TecTrack") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Orden #${state.orden.id}",
                style = MaterialTheme.typography.headlineMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = state.orden.titulo,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "Estado: ${state.orden.estado}"
                    )
                }
            }

            Text(
                text = state.mensaje
            )

            // 8. La UI comunica una acción al ViewModel.
            Button(
                onClick = {
                    vm.avanzarEstado()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Avanzar estado")
            }

            OutlinedButton(
                onClick = {
                    vm.reiniciar()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reiniciar")
            }
        }
    }
}