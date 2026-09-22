// Para que una clase sea heredable o clase padre debe tener
// antepuesta la palabra clave: open class
open class CuentaBancaria(
    val numeroCuenta: Int,
    val cliente: Cliente,
    saldoInicial: Double
) {
    // Encapsulamiento = proteger
    private var saldo: Double = saldoInicial

    // Obtener saldo
    fun obtenerSaldo(): Double {
        return saldo
    }

    // Deposito
    fun depositar(monto: Double) {
        if(monto <= 0){
            // Lanzar = throw
            throw IllegalArgumentException("El monto debe ser mayor a 0")
        }
        saldo += monto
        println("Monto depositado correctamente $$monto")
    }
    // Retiro
    fun retirar(monto: Double) {
        if(monto > saldo){
            throw SaldoInsuficienteException("Saldo Insuficiente, prueba con otro monto")
        }
        saldo -= monto
        println("Retiro exitoso $$monto")
        println("Saldo restante: $$saldo")
    }
    // Declarar un metodo para aplicar polimorfismo
    open fun mostrarTipoCuenta(){
        println("Tipo de cuenta bancaria")
    }
}