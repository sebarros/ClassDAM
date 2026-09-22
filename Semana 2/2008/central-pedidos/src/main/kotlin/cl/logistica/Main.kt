package org.example.cl.logistica
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun mostrarEstado(estado: EstadoPedido){
    when(estado){
        EstadoPedido.Cargando ->
            println("Estado: Procesando pedido")
        is EstadoPedido.Exito ->
            println("Estado: ${estado.mensaje}")
        is EstadoPedido.Error ->
            println("Estado: Error - ${estado.motivo}")
    }
}

// runBlocking: Permite trabajar con corrutinas desde la consola
fun main() = runBlocking {
    println("CENTRAL DE PEDIDOS")
    println("")

    val pedido = Pedido("Ped-910278491", "Pollo asado con papas fritas",
        2,"emailcliente@gmail.com").apply{envioExpress = true}
    println("Pedido creado con apply: $pedido")

    // let = permitir / dejar
    pedido.emailCliente?.let{email ->
        println("Confirmacion enviada a: $email")
    }

    mostrarEstado(EstadoPedido.Cargando)

    // val
    val tiempo = measureTimeMillis{
        val estadoFinal = ProcesadorPedido.procesar(pedido)
        mostrarEstado(estadoFinal)
    }
    println("Tiempo total: $tiempo milisegundos")
}