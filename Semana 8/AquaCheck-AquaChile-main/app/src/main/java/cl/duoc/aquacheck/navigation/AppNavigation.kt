package cl.duoc.aquacheck.navigation

// ARCHIVO: Navigation Compose: rutas y recorrido entre todas las pantallas.

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.aquacheck.model.Rol
import cl.duoc.aquacheck.ui.screens.ChecklistScreen
import cl.duoc.aquacheck.ui.screens.DetalleScreen
import cl.duoc.aquacheck.ui.screens.HistorialScreen
import cl.duoc.aquacheck.ui.screens.InicioScreen
import cl.duoc.aquacheck.ui.screens.LoginScreen
import cl.duoc.aquacheck.ui.screens.NuevoPreChequeoScreen
import cl.duoc.aquacheck.ui.screens.PostChequeoScreen
import cl.duoc.aquacheck.ui.screens.ResultadoScreen
import cl.duoc.aquacheck.ui.screens.SaludScreen
import cl.duoc.aquacheck.viewmodel.SesionViewModel

// region: rutas
/** Nombres de las rutas. Las pantallas con id usan el patrón "ruta/{id}". */
object Rutas {
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val HISTORIAL = "historial"
    const val NUEVO = "nuevo"
    const val CHECKLIST = "checklist/{id}"
    const val SALUD = "salud/{id}"
    const val RESULTADO = "resultado/{id}"
    const val DETALLE = "detalle/{id}"
    const val POST = "post/{id}"

    fun checklist(id: Long) = "checklist/$id"
    fun salud(id: Long) = "salud/$id"
    fun resultado(id: Long) = "resultado/$id"
    fun detalle(id: Long) = "detalle/$id"
    fun post(id: Long) = "post/$id"
}

private val argumentoId = listOf(navArgument("id") { type = NavType.LongType })
private fun NavBackStackEntry.id(): Long = arguments?.getLong("id") ?: 0L
// endregion

@Composable
fun AppNavigation() {
    val nav = rememberNavController()
    // ViewModel de la sesión: vive mientras la Activity esté abierta
    val sesionVm: SesionViewModel = viewModel()
    val sesion by sesionVm.uiState.collectAsStateWithLifecycle()

    NavHost(navController = nav, startDestination = Rutas.LOGIN) {

        // region: login
        composable(Rutas.LOGIN) {
            LoginScreen(sesionVm) {
                nav.navigate(Rutas.INICIO) { popUpTo(Rutas.LOGIN) { inclusive = true } }
            }
        }
        // endregion

        // region: inicio
        composable(Rutas.INICIO) {
            sesion.usuario?.let { usuario ->
                InicioScreen(
                    usuario = usuario,
                    onNuevo = { nav.navigate(Rutas.NUEVO) },
                    onHistorial = { nav.navigate(Rutas.HISTORIAL) { launchSingleTop = true } },
                    onDetalle = { nav.navigate(Rutas.detalle(it)) },
                    onSalir = {
                        sesionVm.cerrarSesion()
                        nav.navigate(Rutas.LOGIN) { popUpTo(Rutas.INICIO) { inclusive = true } }
                    }
                )
            }
        }
        // endregion

        // region: historial
        composable(Rutas.HISTORIAL) {
            HistorialScreen(
                onInicio = { nav.popBackStack(Rutas.INICIO, inclusive = false) },
                onDetalle = { nav.navigate(Rutas.detalle(it)) }
            )
        }
        // endregion

        // region: nuevo
        composable(Rutas.NUEVO) {
            sesion.usuario?.let { usuario ->
                NuevoPreChequeoScreen(
                    usuario = usuario,
                    onVolver = { nav.popBackStack() },
                    onCreado = { id ->
                        nav.navigate(Rutas.checklist(id)) { popUpTo(Rutas.NUEVO) { inclusive = true } }
                    }
                )
            }
        }
        // endregion

        // region: checklist
        composable(Rutas.CHECKLIST, arguments = argumentoId) { entry ->
            val id = entry.id()
            ChecklistScreen(id, onVolver = { nav.popBackStack() }, onContinuar = { nav.navigate(Rutas.salud(id)) })
        }
        // endregion

        // region: salud
        composable(Rutas.SALUD, arguments = argumentoId) { entry ->
            val id = entry.id()
            SaludScreen(id, onVolver = { nav.popBackStack() }, onContinuar = { nav.navigate(Rutas.resultado(id)) })
        }
        // endregion

        // region: resultado
        composable(Rutas.RESULTADO, arguments = argumentoId) { entry ->
            val id = entry.id()
            ResultadoScreen(
                id,
                onVolver = { nav.popBackStack() },
                onConfirmado = {
                    // Vamos al detalle y dejamos solo Inicio atrás en la pila
                    nav.navigate(Rutas.detalle(id)) { popUpTo(Rutas.INICIO) }
                }
            )
        }
        // endregion

        // region: detalle
        composable(Rutas.DETALLE, arguments = argumentoId) { entry ->
            val id = entry.id()
            DetalleScreen(
                id = id,
                rol = sesion.usuario?.rol ?: Rol.ADMIN,   // sin sesión: el rol de menos permisos
                onVolver = { nav.popBackStack() },
                onContinuar = { nav.navigate(Rutas.checklist(id)) },
                onPostChequeo = { nav.navigate(Rutas.post(id)) },
                onEliminado = { nav.popBackStack() }
            )
        }
        // endregion

        // region: post
        composable(Rutas.POST, arguments = argumentoId) { entry ->
            PostChequeoScreen(entry.id(), onVolver = { nav.popBackStack() }, onGuardado = { nav.popBackStack() })
        }
        // endregion
    }
}
