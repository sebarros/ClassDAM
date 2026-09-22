package com.duoc.mvvm.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.mvvm.ui.components.SolicitudCard
import com.duoc.mvvm.viewmodel.SolicitudViewModel

@Composable
fun SolicitudesScreen(viewModel: SolicitudViewModel= viewModel() ){

    //importar la lista
    val solicitudes = viewModel.solicitudes

    Scaffold {innerPadding: PaddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)
        ) {
            Text(text= "Gestión de solicitudes")

            Button(onClick = {viewModel.agregarSolicitud()}) {Text(text= "Agregar Solicitud") }

            solicitudes.forEach { solicitud ->
                SolicitudCard(solicitud = solicitud)
            }
        }

    }
}