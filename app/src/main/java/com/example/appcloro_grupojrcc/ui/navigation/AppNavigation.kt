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
import android.app.Application
import androidx.compose.ui.platform.LocalContext
import com.example.appcloro_grupojrcc.ui.viewmodel.MedicionesViewModel
import com.example.appcloro_grupojrcc.ui.viewmodel.MedicionesViewModelFactory
import com.example.appcloro_grupojrcc.ui.viewmodel.AlertasViewModel
import com.example.appcloro_grupojrcc.ui.viewmodel.AlertasViewModelFactory
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import com.example.appcloro_grupojrcc.ui.viewmodel.PanelJefaturaViewModel
import com.example.appcloro_grupojrcc.ui.viewmodel.PanelJefaturaViewModelFactory
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AppNavigation(){
    val nav = rememberNavController()
    val vm: CloroViewModel = viewModel()
    val medicionesVm: MedicionesViewModel = viewModel(
        factory = MedicionesViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    val alertasVm: AlertasViewModel = viewModel(
        factory = AlertasViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    val panelVm: PanelJefaturaViewModel = viewModel(
        factory = PanelJefaturaViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    val argId = listOf(navArgument("id") {type = NavType.IntType})

    val ultimas by medicionesVm.ultimas.collectAsStateWithLifecycle()
    val pendientes by medicionesVm.pendientes.collectAsStateWithLifecycle()
    val sincronizando by medicionesVm.sincronizando.collectAsStateWithLifecycle()
    val mensajeSync by medicionesVm.mensajeSync.collectAsStateWithLifecycle()
    val contexto = LocalContext.current
    LaunchedEffect(mensajeSync) {
        mensajeSync?.let {
            Toast.makeText(contexto, it, Toast.LENGTH_SHORT).show()
            medicionesVm.mensajeLeido()
        }
    }

    NavHost(
        navController = nav,
        startDestination = Rutas.LOGIN,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300)) +
                    fadeIn(tween(300))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300)) +
                    fadeOut(tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300)) +
                    fadeIn(tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300)) +
                    fadeOut(tween(300))
        }
    ) {
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
                onAlertas = { nav.navigate(Rutas.ALERTAS) },
                onPanel = { nav.navigate(Rutas.PANEL) },
                ultimas = ultimas,
                pendientes = pendientes,
                sincronizando = sincronizando,
                onSincronizar = { medicionesVm.sincronizar() }
            )
            }
        composable (Rutas.DETALLE, arguments = argId) { entry ->
            val id = entry.arguments?.getInt("id") ?: 0
            DetalleScreen(
                vm, id,
                onVolver = {nav.popBackStack() },
                onHistorial = {nav.navigate(Rutas.historial(id)) },
                onRegistro = { nav.navigate(Rutas.registroMedicion(id)) },
                ultimas = ultimas
            )
        }
        composable(Rutas.HISTORIAL, arguments = argId) { entry ->
            HistorialScreen(
                entry.arguments?.getInt("id") ?: 0,
                vm,
                medicionesVm,
                onVolver = { nav.popBackStack() }
            )
        }

        composable(Rutas.REGISTRO_MEDICION, arguments = argId) { entry ->
            RegistroMedicionScreen(
                entry.arguments?.getInt("id") ?: 0,
                vm,
                medicionesVm,
                onVolver = { nav.popBackStack() }
            )
        }

        composable (Rutas.ALERTAS){
            AlertasScreen(
                alertasVm = alertasVm,
                cloroVm = vm,
                onAccion = { nav.navigate(Rutas.accion(it)) },
                onVolver = { nav.popBackStack() }
            )
        }

        composable(
            Rutas.ACCION,
            arguments = listOf(navArgument("alertaId") {type = NavType.IntType})
        ) { entry ->
            AccionScreen(
                alertaId = entry.arguments?.getInt("alertaId") ?: 0,
                cloroVm = vm,
                alertasVm = alertasVm,
                onVolver = { nav.popBackStack() }
            )
        }
        composable(Rutas.PANEL) {
            PanelJefaturaScreen(panelVm, onVolver = { nav.popBackStack() })
        }
    }
}