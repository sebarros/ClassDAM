import kotlinx.coroutines.runBlocking

fun main() {
    val gestor = GestorCentros()
    var opcion: Int
    do {
        println("========== MENU PRINCIPAL ==========")
        println("1. Registrar centro")
        println("2. Listar centros")
        println("3. Buscar centro por ID")
        println("4. Consultar produccion en servidor")
        println("5. Salir")
        print("Seleccione una opcion: ")
        opcion = readln().toIntOrNull() ?: 0

        when (opcion){
            1 -> {
                println("--- Registrar centro ---")
                print("Ingrese ID: ")
                val id = readln()
                print("Ingrese nombre: ")
                val nombre = readln()
                print("Ingrese ubicacion: ")
                val ubicacion = readln()
                print("Ingrese produccion en toneladas: ")
                val produccion = readln().toIntOrNull()
                if (produccion == null) {println("Error: debe ingresar un numero valido.")
                } else {
                    print("Ingrese encargado: ")
                    val encargadoInput = readln()
                    val encargado = if (encargadoInput.isBlank()){null }
                    else{encargadoInput}
                    val centro = CentroCultivo(id = id, nombre = nombre, ubicacion = ubicacion, produccionToneladas = produccion, encargado = encargado).apply{}
                    gestor.agregar(centro)
                    centro.also {println("Centro registrado exitosamente.")}
                }
                println()
            }
            2 -> {
                println("--- Lista de centros ---")
                val centros = gestor.listar()
                if (centros.isEmpty()) {println("No hay centros registrados.")
                } else {
                    for (centro in centros) {
                        println("ID: ${centro.id}")
                        println("Nombre: ${centro.nombre}")
                        println("Ubicacion: ${centro.ubicacion}")
                        println("Produccion: " + "${centro.produccionToneladas} toneladas")
                        centro.encargado?.let {println("Encargado: $it")} ?: println("Encargado no asignado.") }
                }
                println()
            }
            3 -> {
                println("--- Buscar centro ---")
                print("Ingrese el ID: ")
                val id = readln()
                val centro = gestor.buscarPorId(id)
                centro?.let {
                    println("Centro encontrado:")
                    println("ID: ${it.id}")
                    println("Nombre: ${it.nombre}")
                    println("Ubicacion: ${it.ubicacion}")
                    println("Produccion: " + "${it.produccionToneladas} toneladas")
                    it.encargado?.let { encargado -> println("Encargado: $encargado")
                    } ?: println("Encargado no asignado.")
                } ?: println("No se encontro el centro.")
                println()
            }
            4 -> {
                println("--- Consultar produccion ---")
                print("Ingrese el ID del centro: ")
                val id = readln()
                val centro = gestor.buscarPorId(id)
                if (centro != null) {runBlocking{
                        val produccion = ServidorSalmonera.consultarProduccion(centro)
                        println("Produccion obtenida: " + "$produccion toneladas")
                    }
                }else {println("No se encontro el centro.")}
                println()
            }
            5 -> {println("Saliendo del programa...")}
            else -> {println("Opcion no valida.")}
        }
    } while (opcion != 5)
}