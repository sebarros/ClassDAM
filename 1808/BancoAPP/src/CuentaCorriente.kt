class CuentaCorriente(
    numeroCuenta: Int,
    cliente: Cliente,
    saldoInicial: Double,
    var limiteTransferencia: Double
) : CuentaBancaria(numeroCuenta, cliente, saldoInicial) {
    override fun mostrarTipoCuenta() {
        println("Cuenta corriente")
        println("Limite de transferencia: $limiteTransferencia")
    }
}