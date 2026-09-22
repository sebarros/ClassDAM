package com.example.clase4.ui
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clase4.model.AppSection
import com.example.clase4.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Obtenemos la instacia del viewModel
fun HomeScreen(vm: HomeViewModel = viewModel()){
    // Estado para poder controlar este menu de cajon -> inicia cerrado
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Estado para mostrar el snackbar
    val snackbar = remember { SnackbarHostState() }

    // Corrutina que me va a permitir abrir y cerrar el cajon = menu -> mostrar el mensaje del snackbar
    val scope = rememberCoroutineScope()

    // crear el menu lateral de la app
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Superficie visual del menu
            ModalDrawerSheet {
                Text(text = "Nombre Aplicacion", modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge)
                AppSection.entries.forEach { item ->
                    NavigationDrawerItem(
                        label = {Text(item.titulo)},
                        selected = vm.seccion==item,
                        onClick = {
                            vm.seleccionar(item)
                            scope.launch {
                                drawerState.close()
                                snackbar.showSnackbar(message = "Seccion: ${item.titulo}")
                            }
                        }
                    )
                }

            }
        }
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbar)
            }
        ) {padding->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(onClick = {scope.launch { drawerState.open() }}) {Text("M=")}

                Text(text = vm.seccion.titulo, style = MaterialTheme.typography.headlineMedium)
                Card(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Contenido de : ${vm.seccion.titulo}", modifier = Modifier.padding(20.dp))
                }
                Button(onClick = {scope.launch { snackbar.showSnackbar(message = vm.obtenerMensaje()) }}) {Text("Mostrar mensaje en SnackBar")}
            }
        }
    }
}