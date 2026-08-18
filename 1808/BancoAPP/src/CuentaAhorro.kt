class CuentaAhorro(
    numeroCuenta: Int,
    cliente: Cliente,
    saldoInicial: Double,
    val tasaInteres: Double

    // Para heredar de una clase padre () : nombreClase (atributosHeredados)
) : CuentaBancaria(numeroCuenta, cliente, saldoInicial) {
    // Heredar el metodo para aplicar polimorfismo = override
    override fun mostrarTipoCuenta() {
        println("Cuenta de ahorro")
        println("Tasa de interes: $tasaInteres")
    }

}