class GestorCentros {
    val listaCentros: MutableList<CentroCultivo> = mutableListOf()

    fun agregar(centro: CentroCultivo){
        listaCentros.add(centro)
    }

    fun listar(): List<CentroCultivo>{
        return listaCentros
    }

    fun buscarPorId(id: String): CentroCultivo?{
        return listaCentros.find{it.id == id}
    }
}