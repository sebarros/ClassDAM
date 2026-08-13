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
    println("Estado_ $estado")

    // When: permite evaluar multiples condiciones
    val puntaje = 85

}