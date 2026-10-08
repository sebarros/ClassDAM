package cl.duoc.aquacheck

// ARCHIVO: Punto de entrada: crea la Activity, aplica el tema y muestra la navegación.

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.duoc.aquacheck.navigation.AppNavigation
import cl.duoc.aquacheck.ui.theme.AquaCheckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AquaCheckTheme {
                AppNavigation()
            }
        }
    }
}
