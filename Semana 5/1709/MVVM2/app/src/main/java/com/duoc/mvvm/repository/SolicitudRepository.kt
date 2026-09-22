package com.duoc.mvvm.repository

import com.duoc.mvvm.model.Solicitud

class SolicitudRepository {

    private val solicitudes = mutableListOf(
        Solicitud(
            id= 1,
            titulo = "solicitud de contacto",
            descripcion = "Ejemplo transversal de proyecto"
        )
    )

    //obtener
    fun obtenerSolicitudes(): List<Solicitud>{
        return solicitudes.toList()
    }

    //agregar
    fun agregar(solicitud: Solicitud){
        solicitudes.add(solicitud)
    }

}