fun main(){
    println("BIENVENIDO A PUERTO GAMES")

    // 1 - apply: me permite aplicar, configurar, una lista y retornar la misma
    val productos = mutableListOf<String>().apply {
        add("Nintendo 64")
        add("PlayStation 2")
        add("Sega Genesis")
        add("Teclado Mecanico")
        add("Mouse Gamer")
    }

    // 2 - also = tambien permitir realizar una accion adicional
    productos.also {
        println("Catalogo de productos creado con ${it.size} productos")
    }

    // 3 -
    println("CATALOGO DE PRODUCTOS")

    // with = con, trabajar con una lista sin repetir su nombre
    with(productos) {
        println("La cantidad de productos es: $size")
        forEach {productos ->
            println(" - $productos")
        }
    }

    println("Ingresa el producto a buscar")
    val nombreBuscado = readln()
    val productoEncontrado = productos.find {
        it.equals(nombreBuscado, ignoreCase = true)
    }

    // 4 - let = dejar, Dejar que se ejecute solamente si el producto no es null
    productoEncontrado?.let {
        println("Producto encontrado: $it")
    } ?: println("Producto no encontrado")

    println("RESUMEN")
    // Run = correr // ejecutar, ejecuta estas operaciones y devuelve un resultado
    val resumen = productos.run {
        val accesorios = count{
            it.contains("Teclado")||it.contains("Mouse")
        }
        """
            Total de productos: $size
            Accesorios disponibles: $accesorios
            Primer producto: ${first()}
        """.trimIndent()
    }
    println(resumen)
    println("GRACIAS POR COMPRAR EN PUERTO GAMES")
}