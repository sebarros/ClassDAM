fun main(){
    val cliente = Cliente("Marcelo", "16.415.234-5")
    // Cuentas bancarias
    val cuentaAhorro = CuentaAhorro(1001, cliente, 100000.0, 3.5)
    val cuentaCorriente = CuentaCorriente(2001, cliente, 100000.0, 500000.0)

    // Polimorfismo
    val cuentas: List<CuentaBancaria> = listOf(cuentaAhorro, cuentaCorriente)
    for (cuenta in cuentas) {
        println("Lista de cuentas")
        cuenta.mostrarTipoCuenta()
    }

    // Manejos de errores: try, catch, finally
    try {
        println("Ingresa el monto a retirar")
        val monto = readln().toDouble()
        cuentaAhorro.retirar(monto)
        println("Saldo: $${cuentaAhorro.obtenerSaldo()}")
    }catch (error: NumberFormatException){
        println("Error: ingresa un numero por favor")
    }catch (error: SaldoInsuficienteException){
        println("Error: ${error.message}")
    }finally {
        println("Operacion bancaria finalizada")
    }

    println("Programa finalizado")
}