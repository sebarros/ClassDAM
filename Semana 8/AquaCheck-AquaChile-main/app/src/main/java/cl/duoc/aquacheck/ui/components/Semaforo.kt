package cl.duoc.aquacheck.ui.components

// ARCHIVO: Colores del semáforo (verde, ámbar, rojo) y etiqueta Insignia.

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.aquacheck.model.EstadoItem
import cl.duoc.aquacheck.model.NivelSalud
import cl.duoc.aquacheck.model.Resultado
import cl.duoc.aquacheck.ui.theme.Ambar
import cl.duoc.aquacheck.ui.theme.Gris
import cl.duoc.aquacheck.ui.theme.Rojo
import cl.duoc.aquacheck.ui.theme.Verde

fun EstadoItem.color(): Color = when (this) {
    EstadoItem.CUMPLE -> Verde
    EstadoItem.OBSERVADO -> Ambar
    EstadoItem.NO_CUMPLE -> Rojo
    EstadoItem.NO_APLICA -> Gris
    EstadoItem.PENDIENTE -> Gris
}

fun Resultado.color(): Color = when (this) {
    Resultado.CUMPLE -> Verde
    Resultado.OBSERVADO -> Ambar
    Resultado.REQUIERE_REVISION -> Rojo
}

fun NivelSalud.color(): Color = when (this) {
    NivelSalud.OK -> Verde
    NivelSalud.ALERTA -> Ambar
    NivelSalud.CRITICO -> Rojo
}

/** Etiqueta pequeña con color de fondo suave (para estados y resultados). */
@Composable
fun Insignia(texto: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = texto,
            color = color,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
