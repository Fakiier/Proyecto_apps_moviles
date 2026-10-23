package com.example.appcloro_grupojrcc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appcloro_grupojrcc.ui.screen.DetalleScreen
import com.example.appcloro_grupojrcc.ui.screen.LoginScreen
import com.example.appcloro_grupojrcc.ui.screen.PuntosScreen
import com.example.appcloro_grupojrcc.ui.viewmodel.CloroViewModel

@Composable
fun AppNavigation(){
    val nav = rememberNavController()
    val vm: CloroViewModel = viewModel()

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
                }
            )
            }
        composable (
            Rutas.DETALLE,
            arguments = listOf(navArgument("id") { type = NavType.IntType})
        ) { entry ->
            DetalleScreen(vm, entry.arguments?.getInt("id") ?: 0, onVolver = { nav.popBackStack() })
        }
    }
}