package cl.duoc.aquacheck

// ARCHIVO: Clase Application: crea una sola vez el repositorio que usan los ViewModels.

import android.app.Application
import cl.duoc.aquacheck.data.local.AquaCheckDatabase
import cl.duoc.aquacheck.data.repository.PreChequeoRepository

/** Crea el repositorio una sola vez; los ViewModels lo obtienen desde aquí (sin librerías de DI). */
class AquaCheckApp : Application() {
    val repositorio by lazy { PreChequeoRepository(AquaCheckDatabase.obtener(this)) }
}
