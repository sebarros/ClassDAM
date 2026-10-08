package cl.duoc.aquacheck

// ARCHIVO: Pruebas unitarias del login (ViewModel de sesión).

import cl.duoc.aquacheck.model.Rol
import cl.duoc.aquacheck.viewmodel.SesionViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class SesionViewModelTest {

    @Test
    fun loginCorrecto_guardaElUsuarioConSuRol() {
        val vm = SesionViewModel()
        vm.login("supervisor", "1234")
        assertEquals(Rol.SUPERVISOR, vm.uiState.value.usuario?.rol)
        assertNull(vm.uiState.value.error)
    }

    @Test
    fun loginIncorrecto_muestraError_ySinUsuario() {
        val vm = SesionViewModel()
        vm.login("supervisor", "0000")
        assertNull(vm.uiState.value.usuario)
        assertNotNull(vm.uiState.value.error)
    }
}
