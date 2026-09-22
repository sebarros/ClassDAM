package com.duoc.mvvm.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.duoc.mvvm.model.Solicitud

@Composable
fun SolicitudCard(solicitud: Solicitud){
    Card(
        modifier = Modifier.fillMaxSize().padding(vertical= 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(text = solicitud.titulo)
            Text(text = solicitud.descripcion)
            Text(text = "Estado: ${solicitud.estado}")
        }
    }
}