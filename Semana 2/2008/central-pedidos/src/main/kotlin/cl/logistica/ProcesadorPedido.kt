package org.example.cl.logistica
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

object ProcesadorPedido {
    // Funciones para procesar un pedido
    // Suspend funcion que puede detenerse
    private suspend fun consultarStock(pedido: Pedido): Boolean {
        println("Consultando Inventario..")
        // Delay
        delay(2000)
        return pedido.cantidad <= 5
    }

    // Funcion autorizacion de un pago
    private suspend fun autorizarPago(): Boolean{
        println("Autorizando Pago..")
        delay(1500)
        return true
    }

    // Orquestador de las funciones suspendidas en este archivo
    suspend fun procesar(pedido: Pedido): EstadoPedido = coroutineScope {
        // async
        val stockPendiente = async {
            consultarStock(pedido)
        }
        val pagoPendiente = async {
            autorizarPago()
        }
        // await
        val hayStock = stockPendiente.await()
        val pagoAprobado = pagoPendiente.await()

        // Retornar los estados permitidos
        when{
            !hayStock ->
                EstadoPedido.Error("No existe el stock suficiente")
            !pagoAprobado ->
                EstadoPedido.Error("El pago fue rechazado")
            else ->
                EstadoPedido.Exito("Pedido ${pedido.id} aprobado")
        }
    }
}