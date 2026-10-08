package cl.duoc.aquacheck.ui.screens

// ARCHIVO: Checklist TPR-24 con progreso y captura de evidencias (cámara o galería).

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.aquacheck.model.EstadoItem
import cl.duoc.aquacheck.ui.components.AquaTopBar
import cl.duoc.aquacheck.ui.components.ItemCard
import cl.duoc.aquacheck.util.Almacenamiento
import cl.duoc.aquacheck.viewmodel.PreChequeoViewModel
import java.io.File

@Composable
fun ChecklistScreen(
    id: Long,
    onVolver: () -> Unit,
    onContinuar: () -> Unit,
    vm: PreChequeoViewModel = viewModel()
) {
    LaunchedEffect(id) { vm.cargar(id) }
    val state by vm.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(state.mensaje) {
        state.mensaje?.let {
            snackbar.showSnackbar(it)
            vm.consumirMensaje()
        }
    }

    // --- Evidencia fotográfica: cámara del sistema y galería ---
    var itemActivo by rememberSaveable { mutableStateOf<Long?>(null) }
    var rutaCamara by rememberSaveable { mutableStateOf<String?>(null) }

    val camara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { tomada ->
        val item = itemActivo
        val ruta = rutaCamara
        if (tomada && item != null && ruta != null) {
            vm.guardarFoto(item, Almacenamiento.comoTexto(File(ruta)))
        }
    }
    val galeria = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        val item = itemActivo
        if (uri != null && item != null) vm.importarFoto(item, uri)
    }

    fun abrirCamara(itemId: Long) {
        itemActivo = itemId
        val archivo = Almacenamiento.nuevoArchivo(context)
        rutaCamara = archivo.absolutePath
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
        try {
            camara.launch(uri)
        } catch (e: Exception) {
            vm.mostrarMensaje("No se encontró una cámara. Usa la galería.")
        }
    }

    fun abrirGaleria(itemId: Long) {
        itemActivo = itemId
        galeria.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    val evaluados = state.items.count { it.estado != EstadoItem.PENDIENTE }
    val total = state.items.size

    Scaffold(
        topBar = { AquaTopBar("Checklist TPR-24", onVolver) },
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Column(Modifier.navigationBarsPadding().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("$evaluados de $total ítems evaluados", style = MaterialTheme.typography.labelLarge)
                    LinearProgressIndicator(
                        progress = { if (total == 0) 0f else evaluados / total.toFloat() },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(onClick = { vm.validarChecklist(onContinuar) }, modifier = Modifier.fillMaxWidth()) {
                        Text("Continuar: salud del buzo")
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            state.items.groupBy { it.categoria }.forEach { (categoria, lista) ->
                item(key = categoria) {
                    Text(categoria, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                }
                items(lista, key = { it.id }) { elemento ->
                    ItemCard(
                        item = elemento,
                        onEstado = { vm.cambiarEstado(elemento.id, it) },
                        onObservacion = { vm.cambiarObservacion(elemento.id, it) },
                        onCamara = { abrirCamara(elemento.id) },
                        onGaleria = { abrirGaleria(elemento.id) }
                    )
                }
            }
        }
    }
}
