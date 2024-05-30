package com.example.marsphotos.ui.Nav

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marsphotos.data.PantallaPrincipal
import com.example.marsphotos.data.ViewModelLocal
import com.example.marsphotos.ui.screens.CargaAcademicaList
//import com.example.marsphotos.ui.screens.CargaAcademicaList
import com.example.marsphotos.ui.screens.PantallaCalificaciones
import com.example.marsphotos.ui.screens.PantallaInicio
import com.example.marsphotos.ui.screens.PantallaSesion


@Composable
fun App(
    viewModel: PantallaPrincipal = viewModel(factory = PantallaPrincipal.Factory),
    viewModel2: ViewModelLocal = viewModel(factory = ViewModelLocal.Factory),
    context: Context
) {
    val navController = rememberNavController()
    val myViewModel: PantallaPrincipal = viewModel
    val myViewModel2: ViewModelLocal = viewModel2

    NavHost(
        navController = navController,
        startDestination = PantallasNav.Login.route
    ) {
        composable(PantallasNav.Login.route) {
            PantallaInicio(myViewModel,navController,context)
        }
        composable(PantallasNav.Session.route) {
            PantallaSesion(myViewModel2,myViewModel, modifier = Modifier,navController,context)
            //PantallaSesion(alumno = ALUMNO)
        }
        composable(PantallasNav.Carga.route) {
            CargaAcademicaList(myViewModel,navController)

        }
        composable(PantallasNav.Calificacion.route) {
            PantallaCalificaciones(myViewModel, modifier = Modifier,navController)

        }
    }
}
