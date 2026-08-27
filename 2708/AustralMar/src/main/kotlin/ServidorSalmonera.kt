import kotlinx.coroutines.delay

object ServidorSalmonera {
    suspend fun consultarProduccion(centro: CentroCultivo): Int {
        println("Consultando produccion en el servidor...")
        delay(2000)
        return centro.produccionToneladas
    }
}
