package cl.duoc.aquacheck.ui.screens

// ARCHIVO: Pantalla de inicio de sesión.

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.aquacheck.R
import cl.duoc.aquacheck.ui.components.CampoTexto
import cl.duoc.aquacheck.ui.theme.DegradadoMarca
import cl.duoc.aquacheck.viewmodel.SesionViewModel

// Pantalla de inicio de sesión: cabecera con degradado de marca y tarjeta con el formulario.
@Composable
fun LoginScreen(sesionVm: SesionViewModel, onIngreso: () -> Unit) {
    val state by sesionVm.uiState.collectAsStateWithLifecycle()
    var usuario by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }

    // Cuando el ViewModel guarda un usuario, avanzamos a la pantalla de inicio
    LaunchedEffect(state.usuario) {
        if (state.usuario != null) onIngreso()
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Column(
            modifier = Modifier.fillMaxWidth().background(DegradadoMarca).padding(top = 72.dp, bottom = 72.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Logo AquaCheck",
                modifier = Modifier.size(96.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.15f))
            )
            Text("AquaCheck", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Pre-chequeo de seguridad en buceo", color = Color.White.copy(alpha = 0.85f))
        }
        Card(Modifier.padding(horizontal = 20.dp).offset(y = (-48).dp)) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Iniciar sesión", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                CampoTexto(usuario, { usuario = it }, "Usuario")
                // Clave: mismo campo, con texto oculto
                androidx.compose.material3.OutlinedTextField(
                    value = clave,
                    onValueChange = { clave = it },
                    label = { Text("Clave") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                Button(onClick = { sesionVm.login(usuario, clave) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Ingresar")
                }
            }
        }
        Text(
            "MVP académico con datos ficticios\nDemo: supervisor / 1234  ·  admin / 1234",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        )
    }
}
