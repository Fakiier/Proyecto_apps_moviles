package com.example.appcloro_grupojrcc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appcloro_grupojrcc.ui.screen.AccionScreen
import com.example.appcloro_grupojrcc.ui.screen.AlertasScreen
import com.example.appcloro_grupojrcc.ui.screen.DetalleScreen
import com.example.appcloro_grupojrcc.ui.screen.HistorialScreen
import com.example.appcloro_grupojrcc.ui.screen.LoginScreen
import com.example.appcloro_grupojrcc.ui.screen.PanelJefaturaScreen
import com.example.appcloro_grupojrcc.ui.screen.PuntosScreen
import com.example.appcloro_grupojrcc.ui.screen.RegistroMedicionScreen
import com.example.appcloro_grupojrcc.ui.viewmodel.CloroViewModel

@Composable
fun AppNavigation(){
    val nav = rememberNavController()
    val vm: CloroViewModel = viewModel()
    val argId = listOf(navArgument("id") {type = NavType.IntType})

    NavHost(navController = nav, startDestination = Rutas.LOGIN) {
        composable (Rutas.LOGIN){
            LoginScreen (onIngresar = { correo, clave ->
                val ok = vm.iniciarSesion(correo, clave)
                if (ok) nav.navigate(Rutas.PUNTOS) { popUpTo(Rutas.LOGIN) {inclusive = true } }
                ok
            })
        }
        composable (Rutas.PUNTOS) {
            PuntosScreen(
                vm,
                onPuntoClick = { nav.navigate(Rutas.detalle(it)) },
                onCerrarSesion = {
                    vm.cerrarSesion()
                    nav.navigate(Rutas.LOGIN) { popUpTo(Rutas.PUNTOS) { inclusive = true } }
                },
                onAlertas = { nav.navigate(Rutas.PUNTOS) },
                onPanel = {nav.navigate(Rutas.PANEL)}
            )
            }
        composable (Rutas.DETALLE, arguments = argId) { entry ->
            val id = entry.arguments?.getInt("id") ?: 0
            DetalleScreen(
                vm, id,
                onVolver = {nav.popBackStack() },
                onHistorial = {nav.navigate(Rutas.historial(id)) },
                onRegistro = {nav.navigate(Rutas.registroMedicion(id)) }
            )
        }
        composable(Rutas.HISTORIAL, arguments = argId) { entry ->
            HistorialScreen(entry.arguments?.getInt("id") ?: 0, onVolver = {nav.popBackStack() })
        }

        composable(Rutas.REGISTRO_MEDICION, arguments = argId) { entry ->
            RegistroMedicionScreen(entry.arguments?.getInt("id") ?: 0, onVolver = {nav.popBackStack()})
        }

        composable (Rutas.ALERTAS){
            AlertasScreen(
                onAccion = {nav.navigate(Rutas.accion(it))},
                onVolver = {nav.popBackStack()}
            )
        }

        composable(
            Rutas.ACCION,
            arguments = listOf(navArgument("alertaId") {type = NavType.IntType})
        ) { entry ->
            AccionScreen(entry.arguments?.getInt("alertaId") ?: 0, onVolver = {nav.popBackStack()})
        }
        composable(Rutas.PANEL) {
            PanelJefaturaScreen(onVolver = {nav.popBackStack()})
        }
    }
}