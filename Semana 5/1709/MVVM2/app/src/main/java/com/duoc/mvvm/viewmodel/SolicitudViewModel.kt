package com.duoc.mvvm.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.duoc.mvvm.model.Solicitud
import com.duoc.mvvm.repository.SolicitudRepository

class SolicitudViewModel: ViewModel() {

    //crear una instancia del repository
    private val repository = SolicitudRepository()

    var solicitudes by mutableStateOf(repository.obtenerSolicitudes())
    private set

    //
    fun agregarSolicitud(){
        val nuevaSolicitud = Solicitud(
            id = solicitudes.size + 1, // = 2
            titulo = "Solicitud ${solicitudes.size + 1}",
            descripcion = "Creada desde el ViewModel"
        )

        repository.agregar(nuevaSolicitud)
        solicitudes = repository.obtenerSolicitudes()
    }

}