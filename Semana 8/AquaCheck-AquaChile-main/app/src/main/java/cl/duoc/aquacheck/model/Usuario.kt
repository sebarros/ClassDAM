package cl.duoc.aquacheck.model

// ARCHIVO: Modelo de usuario ficticio para el login.

/** Usuario ficticio de prueba. En un sistema real vendría de un backend. */
data class Usuario(
    val nombre: String,
    val usuario: String,
    val clave: String,
    val rol: Rol
)
