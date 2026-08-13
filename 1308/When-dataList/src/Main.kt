// Data class: clase para almacenar datos
// ?: puede tener valor o puede ser nulo
data class Estudiante(
    val nombre: String,
    val alias: String?
)

fun main(){
    // If como expresion
    val edad = 20
    // If puede entregar directamente un valor
    // ?->if : ->else
    val estado = if(edad >= 18) "Adulto" else "Menos de edad"
    println("Edad: $edad")
    println("Estado: $estado")

    // When: permite evaluar multiples condiciones
    val puntaje = 85
    val nivel = when{
        puntaje >= 90 -> "Nivel Experto"
        puntaje >= 70 -> "Nivel Intermedio"
        puntaje >= 50 -> "Nivel Principiante"
        else -> "Nivel Polluelo"
    }
    println("Puntaje: $puntaje")
    println("Nivel: $nivel")

    // When mas parecido a un Switch -> Menu
    val opcion = 2

    // Comparar opcion con when
    val lenguaje = when(opcion){
        1 -> "Java"
        2 -> "Kotlin"
        3 -> "Python"
        4 -> "JavaScript"
        else -> "Lenguaje desconocido"
    }
    println("Lenguaje seleccionado: $lenguaje")

    // Null Safety
    // String?: puede tener un valor como no lo puede tener
    var usuario: String? = null
    println("Usuario: $usuario")
    usuario = "Chocolate"
    println("Usuario: $usuario")

    // Safe Call
    var usuario2: String? = null
    println("Cantidad de letras: ${usuario2?.length}")
    usuario2 = "Carrasco"
    println("Cantidad de letras: ${usuario2?.length}")

    // Elvis ?: encuentra un valor alternativo si es null
    var nickname: String? = null
    val nombreVisible = nickname ?: "Usuario sin nombre"
    println("Nombre: $nombreVisible")
}