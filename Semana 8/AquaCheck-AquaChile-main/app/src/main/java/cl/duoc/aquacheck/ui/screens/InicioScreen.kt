package cl.duoc.aquacheck.ui.screens

// ARCHIVO: Pantalla de inicio: bienvenida, contadores de resultados y últimos registros.

import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import cl.duoc.aquacheck.ui.theme.DegradadoMarca
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.aquacheck.model.PreChequeo
import cl.duoc.aquacheck.model.Resultado
import cl.duoc.aquacheck.model.Rol
import cl.duoc.aquacheck.model.Usuario
import cl.duoc.aquacheck.ui.components.AquaTopBar
import cl.duoc.aquacheck.ui.components.BarraInferior
import cl.duoc.aquacheck.ui.components.RegistroCard
import cl.duoc.aquacheck.ui.components.color
import cl.duoc.aquacheck.ui.theme.Gris
import cl.duoc.aquacheck.viewmodel.HistorialViewModel

@Composable
fun InicioScreen(
    usuario: Usuario,
    onNuevo: () -> Unit,
    onHistorial: () -> Unit,
    onDetalle: (Long) -> Unit,
    onSalir: () -> Unit,
    vm: HistorialViewModel = viewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AquaTopBar("AquaCheck", acciones = {
                IconButton(onClick = onSalir) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                }
            })
        },
        bottomBar = { BarraInferior(seleccion = 0, onInicio = {}, onHistorial = onHistorial) },
        floatingActionButton = {
            // Solo el supervisor puede crear pre-chequeos
            if (usuario.rol == Rol.SUPERVISOR) {
                ExtendedFloatingActionButton(
                    onClick = onNuevo,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Nuevo pre-chequeo") }
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Tarjeta de bienvenida con el degradado de marca
                Column(
                    Modifier.fillMaxWidth().clip(MaterialTheme.shapes.large).background(DegradadoMarca).padding(20.dp)
                ) {
                    Text("Hola, ${usuario.nombre}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(usuario.rol.etiqueta, color = Color.White.copy(alpha = 0.85f))
                }
            }
            item { ResumenCard(state.registros) }
            item { Text("Últimos registros", style = MaterialTheme.typography.titleMedium) }
            if (state.registros.isEmpty()) {
                item { Text("Aún no hay pre-chequeos. Crea el primero con el botón inferior.") }
            }
            items(state.registros.take(3), key = { it.id }) { registro ->
                RegistroCard(registro) { onDetalle(registro.id) }
            }
        }
    }
}

@Composable
private fun ResumenCard(registros: List<PreChequeo>) {
    Card(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Contador(registros.count { it.resultado == Resultado.CUMPLE }, "Cumple", Resultado.CUMPLE.color())
            Contador(registros.count { it.resultado == Resultado.OBSERVADO }, "Observado", Resultado.OBSERVADO.color())
            Contador(registros.count { it.resultado == Resultado.REQUIERE_REVISION }, "Revisión", Resultado.REQUIERE_REVISION.color())
            Contador(registros.count { !it.cerrado }, "Borrador", Gris)
        }
    }
}

@Composable
private fun Contador(valor: Int, etiqueta: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$valor", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = color)
        Text(etiqueta, style = MaterialTheme.typography.labelMedium)
    }
}
