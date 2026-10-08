package cl.duoc.aquacheck.ui.theme

// ARCHIVO: Tema Material 3: paleta AquaCheck, formas redondeadas y degradado de marca.

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Paleta definida en la Clase 2 (identidad visual AquaCheck)
val AzulProfundo = Color(0xFF0B3D62)
val Celeste = Color(0xFF00A3C4)
val Fondo = Color(0xFFF3F7F9)
val TextoOscuro = Color(0xFF0F2430)

// Colores del semáforo
val Verde = Color(0xFF17A75A)
val Ambar = Color(0xFFD98A00)
val Rojo = Color(0xFFDE3B40)
val Gris = Color(0xFF6B7C86)

private val Esquema = lightColorScheme(
    primary = AzulProfundo,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6E8F4),
    onPrimaryContainer = AzulProfundo,
    secondary = Celeste,
    onSecondary = TextoOscuro,
    background = Fondo,
    onBackground = TextoOscuro,
    surface = Color.White,
    onSurface = TextoOscuro,
    surfaceVariant = Color(0xFFE3ECF1),
    onSurfaceVariant = Color(0xFF3A4F5C),
    error = Rojo,
    onError = Color.White,
    // Tarjetas blancas sobre fondo gris azulado, barra inferior blanca y selección celeste suave
    surfaceContainerHighest = Color.White,
    surfaceContainer = Color.White,
    secondaryContainer = Color(0xFFCDEFF7),
    onSecondaryContainer = AzulProfundo,
    outlineVariant = Color(0xFFD3E0E7)
)

private val Formas = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

/** Degradado de marca (superficie → profundidad) usado en cabeceras. */
val DegradadoMarca = Brush.linearGradient(listOf(AzulProfundo, Color(0xFF0E6A94), Celeste))

@Composable
fun AquaCheckTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Esquema, shapes = Formas, content = content)
}
