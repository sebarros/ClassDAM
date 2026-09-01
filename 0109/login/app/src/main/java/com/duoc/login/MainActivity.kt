package com.duoc.login
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.login.ui.theme.LoginTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginTheme {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen(){
    // Estado del usuario
    var usuario by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    // Layout principal
    Column(
        modifier = Modifier
            .fillMaxSize() // Utiliza to el ancho disponible
            .background(Color(0xFFEDF2F7)) // Fondo gris
            .padding(24.dp), // dp density pixel el diseño se adapta de manera automatica a las pantallas
        horizontalAlignment = Alignment.CenterHorizontally, // Centrado horizontal
        verticalArrangement = Arrangement.Center // Centrado vertical
    ) {
        // HTML o elementos estructurales
        Image(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "Logotipo de Punisher",
            modifier = Modifier
                .height(100.dp) // Altura
                .padding(bottom = 32.dp) // Espaciado abajo del logo
        )

        // Usuario
        TextField(
            value = usuario,
            onValueChange = {usuario = it}, // Actualiza el estado
            label = { Text("Nombre de usuario") }, // Etiqueta
            singleLine = true // Solo en una linea
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Contraseña
        TextField(
            value = clave,
            onValueChange = {clave = it},
            label = {Text("Contraseña")},
            singleLine = true,
            visualTransformation = PasswordVisualTransformation() // Oculta caracteres
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Mensaje
        Button(onClick = {
            if (usuario == "admin" && clave == "1234"){
                mensaje = "Bienvenido, $usuario"
            }else{
                mensaje = "Usuario o contraseña incorrecta"
            }
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF000000),
                contentColor = Color(0xFFFFFFFF)
            )
        ) {Text("Ingresar")}
        Spacer(modifier = Modifier.height(20.dp))

        // Mensaje de validacion
        if (mensaje.isNotEmpty()){
            Text(
                text = mensaje,
                fontSize = 18.sp,
                color = if(mensaje.contains("Bienvenido,"))Color(0xFFFF0000) else Color(0xFF2E7D32)
            )
            }
        }
}
@Preview(showBackground = true)
@Composable
fun LoginPreview(){
    LoginScreen()
}