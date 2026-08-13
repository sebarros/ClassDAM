// Data class: clase para almacenar datos
// ?: puede tener valor o puede ser nulo
data class Estudiante(
    val nombre: String,
    val alias: String?,
    val puntaje: Int,
    val activo: Boolean
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

    // Smart Cast: Kotlin identifica el tipo sin declararlo
    // Any: cualquiera

    val dato: Any = "String"
    // Comprobar el tipo de dato
    if(dato is String){
        println("Dato: $dato")
        println("Cantidad de caracteres: ${dato.length}")
    }

    // Data Class
    val estudiante = Estudiante(
        nombre = "Carlos",
        alias = null,
        puntaje = 87,
        activo = true
    )

    // Acceder directamente a las propiedades del objeto
    println("Nombre: ${estudiante.nombre}")
    println("Puntaje: ${estudiante.puntaje}")
    println("Activo: ${estudiante.activo}")

    // NullSafety + DataClass
    // Si no existe un alias muestra otro mensaje
    val aliasVisible = estudiante.alias ?: "Sin Alias"
    println("Alias: ${aliasVisible}")

    // When + DataClass
    val nivelEstudiante = when {
        estudiante.puntaje >= 90 -> "Experto"
        estudiante.puntaje >= 70 -> "Intermedio"
        estudiante.puntaje >= 50 -> "Principiante"
        else -> "Pichon"
    }
    println("Nivel Estudiante: $nivelEstudiante")

    // Detector de perfil
    val mensaje = when{
        estudiante.puntaje >= 90 ->
            "⭐ ${estudiante.nombre}: Kotlin Master"
        estudiante.puntaje >= 70 ->
            "🤖 ${estudiante.nombre}: Android Developer"
        estudiante.puntaje >= 50 ->
            "🐤 ${estudiante.nombre}: Kotlin Junior"
        else ->
            "🙉 ${estudiante.nombre}: Android Roockie"
    }
    println(mensaje)
    println("Alias: ${estudiante.alias ?: "Alias no definido"}")
    println("Puntaje: ${estudiante.puntaje}/100")
    println("Nivel: $nivelEstudiante")

    // If tambien puede utilizarse dentro de un string
    println(
        if(estudiante.activo)
        "Estudiante activo"
        else
        "Estudiante inactivo"
    )
}