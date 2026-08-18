class Cliente (
    val nombre: String,
    val rut: String
){
    //Metodo heredable
    fun mostrarInformacion(){
        println("Nombre: $nombre")
        println("Rut: $rut")
    }
}