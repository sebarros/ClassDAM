package cl.duoc.aquacheck.ui.components

// ARCHIVO: Tarjeta de registro para listas y barra de navegación inferior.

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.aquacheck.model.PreChequeo
import cl.duoc.aquacheck.ui.theme.Gris

/** Tarjeta de un registro en listas (inicio e historial). */
@Composable
fun RegistroCard(registro: PreChequeo, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(Modifier.weight(1f)) {
                Text(registro.buzo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(registro.centro, style = MaterialTheme.typography.bodyMedium)
                Text("${registro.fecha}  ${registro.hora}", style = MaterialTheme.typography.bodySmall)
            }
            val resultado = registro.resultado
            if (resultado != null) {
                Insignia(resultado.etiqueta, resultado.color())
            } else {
                Insignia("Borrador", Gris)
            }
        }
    }
}

/** Barra inferior con las dos secciones principales. [seleccion]: 0 = Inicio, 1 = Historial. */
@Composable
fun BarraInferior(seleccion: Int, onInicio: () -> Unit, onHistorial: () -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = seleccion == 0,
            onClick = onInicio,
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = seleccion == 1,
            onClick = onHistorial,
            icon = { Icon(Icons.Default.History, contentDescription = null) },
            label = { Text("Historial") }
        )
    }
}
