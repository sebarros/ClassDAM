package org.example.cl.logistica

data class Pedido(
    val id: String,
    val producto: String,
    val cantidad: Int,
    val emailCliente: String?,
    var envioExpress: Boolean = false
)