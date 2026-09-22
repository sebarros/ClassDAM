package com.example.clase4.viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.clase4.model.AppSection


class HomeViewModel: ViewModel() {
    // Guarda la seccion que esta seleccionada actualmente
    var seccion by mutableStateOf(AppSection.INICIO)
        private set

    // Cambia la seccion seleccionada
    fun seleccionar (nueva: AppSection){
        seccion = nueva
    }

    // Agregar un mensaje al snackbar
    fun obtenerMensaje(): String{
        return "Accion realizada en ${seccion.titulo}"
    }
}