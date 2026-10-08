package cl.duoc.aquacheck.ui.components

// ARCHIVO: Tarjeta de un ítem del checklist: semáforo, observación y fotos.

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.aquacheck.model.EstadoItem
import cl.duoc.aquacheck.model.ItemChequeo
import cl.duoc.aquacheck.ui.theme.Rojo
import coil.compose.AsyncImage

private val opciones = listOf(
    EstadoItem.CUMPLE to "OK",
    EstadoItem.OBSERVADO to "Obs.",
    EstadoItem.NO_CUMPLE to "No",
    EstadoItem.NO_APLICA to "N/A"
)

/** Un ítem del checklist: semáforo, observación y evidencia fotográfica. */
@Composable
fun ItemCard(
    item: ItemChequeo,
    onEstado: (EstadoItem) -> Unit,
    onObservacion: (String) -> Unit,
    onCamara: () -> Unit,
    onGaleria: () -> Unit
) {
    // Copia local del texto: evita saltos del cursor mientras Room guarda
    var observacion by remember(item.id) { mutableStateOf(item.observacion) }
    val pideObservacion = item.estado == EstadoItem.OBSERVADO || item.estado == EstadoItem.NO_CUMPLE
    val permiteFoto = item.estado != EstadoItem.PENDIENTE && item.estado != EstadoItem.NO_APLICA

    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, item.estado.color())
    ) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    item.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                if (item.critico) Insignia("Crítico", Rojo)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                opciones.forEach { (estado, texto) ->
                    FilterChip(
                        selected = item.estado == estado,
                        onClick = { onEstado(estado) },
                        label = { Text(texto, maxLines = 1) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = estado.color(),
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (pideObservacion) {
                CampoTexto(
                    valor = observacion,
                    onCambio = {
                        observacion = it
                        onObservacion(it)
                    },
                    etiqueta = "Observación (obligatoria)",
                    error = if (observacion.trim().length < 5) "Describe el problema (mín. 5 letras)" else null,
                    lineas = 2
                )
            }

            if (permiteFoto) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = onCamara) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = null)
                        Spacer(Modifier.size(4.dp))
                        Text("Cámara")
                    }
                    TextButton(onClick = onGaleria) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(Modifier.size(4.dp))
                        Text("Galería")
                    }
                    Spacer(Modifier.weight(1f))
                    if (item.fotoUri.isNotBlank()) {
                        AsyncImage(
                            model = item.fotoUri,
                            contentDescription = "Evidencia de ${item.nombre}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
        }
    }
}
