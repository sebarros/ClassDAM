package org.example.cl.logistica

sealed class EstadoPedido {
    // Objeto que va a representar el proceso de un pedido
    object Cargando : EstadoPedido()

    // Exito
    data class Exito(val mensaje: String) : EstadoPedido()

    // Error
    data class Error(val motivo: String) : EstadoPedido()
}