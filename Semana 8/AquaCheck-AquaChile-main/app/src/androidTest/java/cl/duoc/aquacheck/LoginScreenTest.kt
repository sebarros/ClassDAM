package cl.duoc.aquacheck

// ARCHIVO: Prueba de interfaz del login con Compose.

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import cl.duoc.aquacheck.ui.screens.LoginScreen
import cl.duoc.aquacheck.ui.theme.AquaCheckTheme
import cl.duoc.aquacheck.viewmodel.SesionViewModel
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun ingresarSinDatos_muestraMensajeDeError() {
        composeRule.setContent {
            AquaCheckTheme { LoginScreen(sesionVm = SesionViewModel(), onIngreso = {}) }
        }
        composeRule.onNodeWithText("Ingresar").performClick()
        composeRule.onNodeWithText("Ingresa tu usuario y clave").assertExists()
    }
}
