fun main(){
    // Funcion: 1 Fun, 2 nombreFuncion, 3 (argumentos: tipoDato?): 4 tipoDatoRetorno {...}
    fun esMayorDeEdad(edad: Int) : Boolean{
        return edad >= 18
    }

    // Funcion de una sola expresion
    fun doble(numero: Int) = numero * 2

    // -----Ciclo For-----
    for(i in 1..100){
        println("Intento: $i")
    }

    // -----Listas-----
    val edades = listOf(18,34,17,22,20,34,16)
    for(edad in edades){
        println("Edad: $edad")
    }

    // -----While-----
    // Inicializador
    var i = 0

    // Size
    while(i < edades.size){
        println("Posicion $i: ${edades[i]}")
        i++
    }

    // For Each: por cada uno
    edades.forEach{edad -> println("Edad: $edad")}

    // Set: elimina los elementos repetidos
    val nombres = listOf(
        "Victoria",
        "Camila",
        "Victoria"
    )
    println("Lista de nombres original: $nombres")
    val nombresUnicos = nombres.toSet()
    println("Lista de nombres con toSet: $nombresUnicos")

    // Map: relaciona una clave con un valor
    val cursos = mapOf(
        "Andres" to "Aplicaciones Moviles",
        "Victor" to "Full Stack II",
        "Ramon" to "Desarrollo orientado a objetos"
    )
    println(cursos)
    // Podemos buscar valores por la clave
    println("Curso de Victor: ${cursos["Victor"]}")

    // Filter: genera una nueva lista solamente con los elementos que cumplen la condicion
    val mayores = edades.filter {edad -> edad >= 18}
    println("Mayores de edad de la lista de edades: $mayores")
}